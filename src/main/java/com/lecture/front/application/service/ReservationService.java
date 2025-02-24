package com.lecture.front.application.service;

import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.validate.ReservationValidator;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import com.lecture.front.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 강연 예약 서비스에서는 단일 서버 환경에서 데이터 일관성을 확보하기 위해 다음과 같이 구현했습니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    @Qualifier("frontReservationRepository")
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final LectureAvailabilityRepository lectureAvailabilityRepository;

    /**
     * 강연 예약 처리
     * 1. 유효성 검증 및 좌석 감소 (기본 트랜잭션)
     * 2. 예약 생성/재활성화 (별도의 트랜잭션에서 upsertReservation 호출)
     */
    @Transactional
    public ReservationResponse reserve(ReservationRequest dto) {
        log.info("예약 요청: {}", dto);

        // 1. 유효성 검증
        Lecture lecture = reservationValidator.validateReservation(dto);

        // 2. 좌석 감소 원자적 업데이트
        int updatedRows = lectureAvailabilityRepository.decrementAvailableSeats(lecture.getId());
        if (updatedRows == 0) {
            log.error("예약 실패: 좌석이 부족합니다. lectureId={}, employeeNumber={}",
                    lecture.getId(), dto.getEmployeeNumber());
            throw new IllegalArgumentException("예약 실패: 좌석이 부족합니다.");
        }

        // 3. 별도 트랜잭션에서 예약 생성/재활성화 처리
        Reservation reservation = upsertReservation(lecture, dto.getEmployeeNumber());
        log.info("예약 처리 성공: reservationId={}", reservation.getId());
        return new ReservationResponse(reservation);
    }

    /**
     * 별도의 트랜잭션(REQUIRES_NEW)에서 예약을 생성하거나 재활성화
     */
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Reservation upsertReservation(Lecture lecture, String employeeNumber) {
        // 조건부 upsert를 native query 등으로 한 번에 처리 (INSERT ... ON DUPLICATE KEY UPDATE)
        int affectedRows = reservationRepository.upsertReservation(lecture.getId(), employeeNumber);
        if (affectedRows > 0) {
            // 예약이 성공적으로 생성 또는 재활성화되었으므로, 활성 예약 정보를 조회하여 반환합니다.
            Reservation reservation = reservationRepository.findActiveReservationByLectureIdAndEmployeeNumber(
                    lecture.getId(), employeeNumber);
            if (reservation != null) {
                return reservation;
            }
        }
        throw new IllegalStateException("예약 처리 중 알 수 없는 오류가 발생했습니다.");
    }

    /**
     * 사번으로 신청 내역 조회
     */
    @Transactional
    public java.util.List<ReservationResponse> getReservationsByEmployee(String employeeNumber) {
        return reservationRepository.findActiveReservationsByEmployeeNumber(employeeNumber).stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long lectureId, String employeeNumber) {
        // 1. 취소 업데이트: 조건부 UPDATE로 활성 예약 상태인 경우에만 CANCELED로 변경
        int updated = reservationRepository.cancelReservation(lectureId, employeeNumber);
        if (updated == 0) {
            log.error("예약 취소 실패: 해당 강연에 대한 활성 예약이 존재하지 않습니다. lectureId={}, employeeNumber={}", lectureId, employeeNumber);
            throw new IllegalArgumentException("예약이 존재하지 않습니다.");
        }
        log.info("예약 취소 성공: lectureId={}, employeeNumber={}", lectureId, employeeNumber);

        // 2. 좌석 복구: lecture_availability 테이블에서 available_seats를 1 증가시키고, reserved_count를 1 감소
        int availabilityUpdated = lectureAvailabilityRepository.incrementAvailableSeats(lectureId);
        if (availabilityUpdated == 0) {
            log.warn("강연 좌석 복구 실패: lecture_availability 업데이트에 실패했습니다. lectureId={}", lectureId);
        } else {
            log.info("강연 좌석 복구 성공: lectureId={}", lectureId);
        }
    }


}
