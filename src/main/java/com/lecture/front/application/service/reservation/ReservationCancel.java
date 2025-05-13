package com.lecture.front.application.service.reservation;

import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import com.lecture.front.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationCancel {
    private final ReservationRepository reservationRepository;
    private final LectureAvailabilityRepository lectureAvailabilityRepository;

    public Reservation cancel(Long lectureId, String employeeNumber) {
        Reservation reservation = reservationRepository
                .findByLectureIdAndEmployeeNumberAndStatus(lectureId, employeeNumber, ReservationStatus.CONFIRMED)
                .orElseThrow(() -> new IllegalArgumentException("활성 예약이 존재하지 않습니다."));

        reservation.cancel();
        log.info("예약 취소 성공: reservationId={}", reservation.getId());

        lectureAvailabilityRepository.incrementAvailableSeats(lectureId);
        return reservation;
    }
}
