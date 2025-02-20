package com.lecture.front.application.service;

import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.validate.ReservationValidator;
import com.lecture.front.domain.model.Lecture;
import com.lecture.front.domain.model.Reservation;
import com.lecture.front.domain.model.ReservationStatus;
import com.lecture.front.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 강연 예약/취소, 사번으로 신청 내역 조회 서비스
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

        // 예약 생성: 기본 상태는 CONFIRMED로 설정
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
     * 신청한 강연 취소: 예약 상태를 CANCELED로 변경
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            log.error("예약 취소 실패: 예약이 존재하지 않습니다. reservationId={}", reservationId);
            throw new IllegalArgumentException("예약이 존재하지 않습니다.");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        log.info("예약 취소 성공: reservationId={}", reservationId);
    }
}
