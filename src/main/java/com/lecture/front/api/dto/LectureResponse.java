package com.lecture.front.api.dto;

import com.lecture.common.domain.model.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class LectureResponse {
    private final Long id;
    private final String lecturer;
    private final String venue;
    private final int capacity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    // 추가적으로 예약 건수를 포함할 수 있음
    private final Long reservationCount;

    public static LectureResponse toResponse(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .lecturer(lecture.getLecturer())
                .venue(lecture.getVenue())
                .capacity(lecture.getCapacity())
                .startTime(lecture.getStartTime())
                .endTime(lecture.getEndTime())
                .build();
    }

    public static LectureResponse toResponse(Lecture lecture, Long reservationCount) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .lecturer(lecture.getLecturer())
                .venue(lecture.getVenue())
                .capacity(lecture.getCapacity())
                .startTime(lecture.getStartTime())
                .endTime(lecture.getEndTime())
                .description(lecture.getDescription())
                .reservationCount(reservationCount)
                .build();
    }

    // 헬퍼 메서드: Object[] -> LectureResponse 변환
    public static LectureResponse mapToLectureResponse(Object[] result) {
        return toResponse((Lecture) result[0], (Long) result[1]);
    }
}
