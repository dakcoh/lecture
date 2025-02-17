package com.lecture.backoffice.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 강연 등록 요청 시
 */
@Getter
@Setter
@ToString
public class LectureRequest {

    @NotNull
    private String lecturer;

    @NotNull
    private String venue;

    @NotNull
    private int capacity;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String description;
}
