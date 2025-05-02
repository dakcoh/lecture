package com.lecture.front.application.service;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.domain.repository.frontLectureRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 인기 강연 조회 서비스: 최근 3일간 신청이 가장 많은 강연 순으로 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PopularLectureService {
    private final frontLectureRepository lectureRepository;

    /**
     * 최근 3일간 신청 건수가 많은 인기 강연을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<LectureResponse> getPopularLectures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);

        // JOIN을 통해 강연과 예약 건수를 함께 조회
        List<Object[]> results = lectureRepository.findPopularLectures(threeDaysAgo, now);

        // 인기 강연 조회
        List<LectureResponse> popularLectures = results.stream()
                .map(LectureResponse::mapToLectureResponse)
                .collect(Collectors.toList());
        log.info("인기 강연 조회: {}건", popularLectures.size());

        return popularLectures;
    }
}
