package com.lecture.front.application.service.reservation;

import com.lecture.front.domain.repository.FrontLectureAvailabilityRepository;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import com.lecture.front.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final ReservationRepository reservationRepository;
    private final FrontLectureAvailabilityRepository lectureAvailabilityRepository;

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
        Reservation reservation = Reservation.builder()
                .id(lecture.getId())
                .employeeNumber(employeeNumber)
                .status(ReservationStatus.CONFIRMED)
                .build();

        Reservation savereservation = reservationRepository.save(reservation);

        log.info("예약 등록 성공: id={}", savereservation);

        return savereservation;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getActiveReservations(String employeeNumber) {
        return reservationRepository.findByEmployeeNumberAndStatus(employeeNumber, ReservationStatus.CONFIRMED);
    }
}
