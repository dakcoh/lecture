package com.lecture.front.application.service;

import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.validate.ReservationValidator;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import com.lecture.front.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ReservationService
 *
 * 강연 예약 서비스에서는 단일 서버 환경에서 데이터 일관성을 확보하기 위해 다음과 같이 구현했습니다.
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    @Qualifier("frontReservationRepository")
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;

    /**
     * 강연 신청 처리
     *
     * @param dto 강연 예약 요청 DTO (강연 ID, 사번)
     * @return 예약 생성 후 반환 DTO
     * @throws IllegalArgumentException 중복 예약, 정원 초과, 또는 시간 겹침이 발생하면 예외 발생
     */
    @Transactional
    public ReservationResponse reserve(ReservationRequest dto) {
        log.info("예약 요청: {}", dto);

        // ReservationValidator에서 유효성 검증 수행 (검증 실패 시 예외 발생)
        reservationValidator.validateReservation(dto);

        // 5. 예약 생성: 기본 상태는 CONFIRMED로 설정
        Reservation reservation = Reservation.builder()
                .lectureId(dto.getLectureId())
                .employeeNumber(dto.getEmployeeNumber())
                .status(ReservationStatus.CONFIRMED)
                .build();
        Reservation saved = reservationRepository.save(reservation);
        log.info("강연 신청 성공: reservationId={}", saved.getId());
        return new ReservationResponse(saved);
    }

    /**
     * 사번으로 신청 내역 조회
     */
    @Transactional
    public java.util.List<ReservationResponse> getReservationsByEmployee(String employeeNumber) {
        return reservationRepository.findByEmployeeNumber(employeeNumber).stream()
                .map(ReservationResponse::new)
                .toList();
    }

    /**
     * 강연 예약 취소: 강연 ID와 사번을 기준으로 예약을 조회한 후, 해당 예약의 상태를 CANCELED로 변경합니다.
     *
     * @param lectureId      예약 취소 대상 강연의 ID
     * @param employeeNumber 예약한 사번 (5자리)
     */
    @Transactional
    public void cancelReservation(Long lectureId, String employeeNumber) {
        Reservation reservation = reservationRepository.findByLectureIdAndEmployeeNumber(lectureId, employeeNumber);
        if (reservation == null) {
            log.error("예약 취소 실패: 해당 강연에 대한 예약이 존재하지 않습니다. lectureId={}, employeeNumber={}", lectureId, employeeNumber);
            throw new IllegalArgumentException("예약이 존재하지 않습니다.");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        log.info("예약 취소 성공: reservationId={}", reservation.getId());
    }
}
