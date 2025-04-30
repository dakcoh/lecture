package com.lecture.backoffice.api.dto;

import com.lecture.common.domain.model.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 강연 조회 응답 시
 */
@Getter
public class LectureResponse {

    private final Long id;
    private final String lecturer;
    private final String venue;
    private final int capacity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;

    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.lecturer = lecture.getLecturer();
        this.venue = lecture.getVenue();
        this.capacity = lecture.getCapacity();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.description = lecture.getDescription();
    }

    public static LectureResponse toResponse(Lecture lecture) {
        return new LectureResponse(lecture);
    }
}
