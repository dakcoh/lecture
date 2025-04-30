package com.lecture.front.application.service.reservation;

import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.Reservation;
import com.lecture.front.domain.repository.frontReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationProcessor {

    private final frontReservationRepository reservationRepository;
    private final LectureAvailabilityRepository lectureAvailabilityRepository;

    public Reservation process(Lecture lecture, String employeeNumber) {
        int updatedRows = lectureAvailabilityRepository.decrementAvailableSeats(lecture.getId());
        if (updatedRows == 0) {
            log.error("예약 실패: 좌석 부족 lectureId={}, emp={}", lecture.getId(), employeeNumber);
            throw new IllegalArgumentException("예약 실패: 좌석 부족");
        }
        return upsertReservation(lecture, employeeNumber);
    }

    @Retryable(backoff = @Backoff(delay = 100))
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Reservation upsertReservation(Lecture lecture, String employeeNumber) {
        int affectedRows = reservationRepository.upsertReservation(lecture.getId(), employeeNumber);
        if (affectedRows > 0) {
            return reservationRepository.findActiveReservationByLectureIdAndEmployeeNumber(lecture.getId(), employeeNumber);
        }
        throw new IllegalStateException("예약 처리 중 오류 발생");
    }

    @Transactional(readOnly = true)
    public List<Reservation> getActiveReservations(String employeeNumber) {
        return reservationRepository.findActiveReservationsByEmployeeNumber(employeeNumber);
    }
}
