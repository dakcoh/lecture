package com.lecture.front.api.dto;

import com.lecture.common.domain.model.Lecture;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LectureResponse {
    private final Long id;
    private final String lecturer;
    private final String venue;
    private final int capacity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    // 추가적으로 예약 건수를 포함할 수 있음
    private Long reservationCount;

    // Lecture 타입을 인자로 받는 생성자
    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.lecturer = lecture.getLecturer();
        this.venue = lecture.getVenue();
        this.capacity = lecture.getCapacity();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.description = lecture.getDescription();
    }
    // 예약 건수를 포함한 생성자
    public LectureResponse(Lecture lecture, Long reservationCount) {
        this(lecture);
        this.reservationCount = reservationCount;
    }

    // 헬퍼 메서드: Object[] -> LectureResponse 변환
    public static LectureResponse mapToLectureResponse(Object[] result) {
        return new LectureResponse((Lecture) result[0], (Long) result[1]);
    }
}
