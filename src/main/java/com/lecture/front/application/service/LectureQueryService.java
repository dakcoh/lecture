package com.lecture.front.application.service;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.domain.repository.LectureRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 강연 조회 서비스: 신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출
 */
@Service
@RequiredArgsConstructor
public class LectureQueryService {

    @Qualifier("frontLectureRepository")
    private final LectureRepository lectureRepository;

    @Transactional
    public List<LectureResponse> getAvailableLectures() {
        LocalDateTime now = LocalDateTime.now();
        // 신청 가능한 기간: 강연 시작 1주일 전부터 강연 시작 1일 후까지
        return lectureRepository.findAll().stream()
                .filter(lecture -> {
                    LocalDateTime availableFrom = lecture.getStartTime().minusWeeks(1);
                    LocalDateTime availableUntil = lecture.getStartTime().plusDays(1);
                    return now.isAfter(availableFrom) && now.isBefore(availableUntil);
                })
                .map(LectureResponse::new)
                .collect(Collectors.toList());
    }
}
