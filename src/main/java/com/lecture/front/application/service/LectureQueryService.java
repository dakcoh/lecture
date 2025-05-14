package com.lecture.front.application.service;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.domain.repository.FrontLectureRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final FrontLectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public List<LectureResponse> getAvailableLectures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusWeeks(1);
        LocalDateTime to = now.plusDays(1);

        return lectureRepository.findByStartTimeBetween(from, to).stream()
                .map(LectureResponse::toResponse)
                .collect(Collectors.toList());
    }
}
