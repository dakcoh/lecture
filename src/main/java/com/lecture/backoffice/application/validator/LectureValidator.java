package com.lecture.backoffice.application.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 강연 등록 요청에 대한 유효성 검증을 수행하는 Validator
 */
@Slf4j
@Component
public class LectureValidator {

    /**
     * 시작 시간과 종료 시간의 유효성을 검증
     * 종료 시간은 시작 시간보다 늦어야 한다.
     *
     * @param startTime 강연 시작 시간
     * @param endTime   강연 종료 시간
     */
    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            log.error("강연 등록 실패: 잘못된 시간 입력, startTime={}, endTime={}", startTime, endTime);
            throw new IllegalArgumentException("잘못된 시간 입력: 종료 시간은 시작 시간보다 늦어야 합니다.");
        }
    }
}
