package com.lecture.backoffice.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예약 상태
 */
@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    CONFIRMED("확정"),
    CANCELED("취소");

    private final String description;
}
