package com.lecture.front.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 강연 예약 요청 DTO
 */
@Getter
@Setter
public class ReservationRequest {
    @NotNull
    private Long lectureId;

    @NotNull
    private String employeeNumber;  // 5자리 사번 (Validate는 없음)
}
