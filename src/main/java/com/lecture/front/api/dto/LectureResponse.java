package com.lecture.front.api.dto;

import com.lecture.common.domain.model.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LectureResponse {
    private final Long id;
    private final String lecturer;
    private final String venue;
    private final int capacity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    // 상태 필드 등 추가

    // Lecture 타입을 인자로 받는 생성자
    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.lecturer = lecture.getLecturer();
        this.venue = lecture.getVenue();
        this.capacity = lecture.getCapacity();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.description = lecture.getDescription();
        // 필요시 추가 필드 초기화
    }
}
