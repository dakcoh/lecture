package com.lecture.backoffice.api.dto;

import com.lecture.backoffice.domain.model.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 강연 조회 응답 시
 */
@Getter
public class LectureResponse {

    private Long id;
    private String lecturer;
    private String venue;
    private int capacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;

    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.lecturer = lecture.getLecturer();
        this.venue = lecture.getVenue();
        this.capacity = lecture.getCapacity();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.description = lecture.getDescription();
    }
}
