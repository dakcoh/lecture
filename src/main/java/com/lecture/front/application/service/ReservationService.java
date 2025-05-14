package com.lecture.front.application.service;

import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.service.reservation.ReservationCancel;
import com.lecture.front.application.service.reservation.ReservationProcessor;
import com.lecture.front.application.validate.ReservationValidator;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 강연 예약 서비스에서는 단일 서버 환경에서 데이터 일관성을 확보하기 위해 다음과 같이 구현했습니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationValidator reservationValidator;
    private final ReservationProcessor reservationProcessor;
    private final ReservationCancel reservationCancel;

    /**
     * 강연 예약 처리
     * 1. 유효성 검증 및 좌석 감소 (기본 트랜잭션)
     * 2. 예약 생성/재활성화 (별도의 트랜잭션에서 upsertReservation 호출)
     */
    @Transactional
    public ReservationResponse reserve(ReservationRequest dto) {
        log.info("예약 요청: {}", dto);

        Lecture lecture = reservationValidator.validateReservation(dto);

        Reservation reservation = reservationProcessor.process(lecture, dto.getEmployeeNumber());
        log.info("예약 처리 성공: reservationId={}", reservation.getId());

        return ReservationResponse.toResponse(reservation);
    }

    /**
     * 사번으로 신청 내역 조회
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByEmployee(String employeeNumber) {
        return reservationProcessor.getActiveReservations(employeeNumber).stream()
                .map(ReservationResponse::toResponse)
                .toList();
    }

    @Transactional
    public ReservationResponse cancelReservation(Long lectureId, String employeeNumber) {
        Reservation reservation = reservationCancel.cancel(lectureId, employeeNumber);
        return ReservationResponse.toResponse(reservation);
    }
}
