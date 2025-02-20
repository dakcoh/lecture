package com.lecture.front.application.service;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.domain.model.Lecture;
import com.lecture.front.domain.repository.LectureRepository;
import com.lecture.front.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    @Qualifier("frontReservationRepository")
    private final ReservationRepository reservationRepository;

    @Transactional
    public List<LectureResponse> getPopularLectures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);

        // 모든 강연 조회 후, 예약 수를 기준으로 정렬
        List<Lecture> lectures = lectureRepository.findAll();
        List<LectureResponse> popularLectures = lectures.stream()
                .sorted(Comparator.comparingLong(
                        lecture -> -reservationRepository.countReservationsForLectureWithinPeriod(
                                lecture.getId(), threeDaysAgo, now)))
                .map(LectureResponse::new)
                .collect(Collectors.toList());
        log.info("인기 강연 조회: {}건", popularLectures.size());
        return popularLectures;
    }
}
