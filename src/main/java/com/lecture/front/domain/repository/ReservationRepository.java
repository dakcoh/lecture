package com.lecture.front.domain.repository;

import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByEmployeeNumberAndStatus(String employeeNumber, ReservationStatus status);

    Optional<Reservation> findByLectureIdAndEmployeeNumberAndStatus(Long lectureId, String employeeNumber, ReservationStatus status);

    long countByLectureIdAndStatus(Long lectureId, ReservationStatus status);

    boolean existsByLectureIdAndEmployeeNumberAndStatus(Long lectureId, String employeeNumber, ReservationStatus status);
}