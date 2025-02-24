package com.lecture.front.application.service;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.common.domain.model.Lecture;
import com.lecture.front.domain.repository.LectureRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("frontLectureRepository")
    private final LectureRepository lectureRepository;

    /**
     * 최근 3일간 신청 건수가 많은 인기 강연을 조회합니다.
     */
    @Transactional
    public List<LectureResponse> getPopularLectures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);

        // JOIN을 통해 강연과 예약 건수를 함께 조회
        List<Object[]> results = lectureRepository.findPopularLectures(threeDaysAgo, now);

        // 인기 강연 조회
        List<LectureResponse> popularLectures = results.stream()
                .map(this::mapToLectureResponse)
                .collect(Collectors.toList());
        log.info("인기 강연 조회: {}건", popularLectures.size());

        return popularLectures;
    }

    // 헬퍼 메서드: Object[] -> LectureResponse 변환
    private LectureResponse mapToLectureResponse(Object[] result) {
        return new LectureResponse((Lecture) result[0], (Long) result[1]);
    }
}
