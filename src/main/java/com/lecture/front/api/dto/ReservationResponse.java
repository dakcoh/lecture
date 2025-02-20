package com.lecture.front.api.dto;

import com.lecture.front.domain.model.Reservation;
import com.lecture.front.domain.model.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 강연 예약 응답 DTO
 */
@Getter
public class ReservationResponse {
    private final Long id;
    private final Long lectureId;
    private final String employeeNumber;
    private final ReservationStatus status;
    private final LocalDateTime createdAt;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.lectureId = reservation.getLectureId();
        this.employeeNumber = reservation.getEmployeeNumber();
        this.status = reservation.getStatus();
        this.createdAt = reservation.getCreatedAt();
    }
}
