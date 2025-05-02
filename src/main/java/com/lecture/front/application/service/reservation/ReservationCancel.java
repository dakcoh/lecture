package com.lecture.front.application.service.reservation;

import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.common.domain.model.Reservation;
import com.lecture.front.domain.repository.frontReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationCancel {
    private final frontReservationRepository reservationRepository;
    private final LectureAvailabilityRepository lectureAvailabilityRepository;

    public Reservation cancel(Long lectureId, String employeeNumber) {
        // 1. 취소 업데이트: 조건부 UPDATE로 활성 예약 상태인 경우에만 CANCELED로 변경
        Reservation reservation = reservationRepository.findActiveReservationByLectureIdAndEmployeeNumber(lectureId, employeeNumber);
        if (reservation == null) {
            throw new IllegalArgumentException("활성 예약이 존재하지 않습니다.");
        }

        reservation.cancel();
        log.info("예약 취소 성공: reservationId={}", reservation.getId());

        lectureAvailabilityRepository.incrementAvailableSeats(lectureId);
        return reservation;
    }
}
