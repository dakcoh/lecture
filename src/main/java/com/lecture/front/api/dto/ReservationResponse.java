package com.lecture.front.api.dto;

import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 강연 예약 응답 DTO
 */
@Getter
@Builder
@RequiredArgsConstructor
public class ReservationResponse {
    private final Long id;
    private final Long lectureId;
    private final String employeeNumber;
    private final ReservationStatus status;
    private final LocalDateTime createdAt;

    public static ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .lectureId(reservation.getLecture().getId())
                .employeeNumber(reservation.getEmployeeNumber())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}
