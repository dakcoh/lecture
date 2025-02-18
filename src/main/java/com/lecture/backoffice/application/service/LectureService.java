package com.lecture.backoffice.application.service;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import com.lecture.backoffice.application.validator.LectureValidator;
import com.lecture.backoffice.domain.model.Lecture;
import com.lecture.backoffice.infrastructure.repository.LectureRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 강연 등록 (강연자, 강연장, 신청 인원, 강연 시간, 강연 내용 입력)
 * 강연 목록 (전체 강연 목록)
 */
@Slf4j
@Service
public class LectureService {

    @Qualifier("backofficeLectureRepository")
    private final LectureRepository lectureRepository;
    private final LectureValidator lectureValidator;

    public LectureService(LectureRepository lectureRepository, LectureValidator lectureValidator) {
        this.lectureRepository = lectureRepository;
        this.lectureValidator = lectureValidator;
    }

    // 강연 등록
    @Transactional
    public LectureResponse createLecture(LectureRequest dto) {
        log.info("강연 등록 요청: {}", dto.toString());
        // 시간 범위 검증
        lectureValidator.validateTimeRange(dto.getStartTime(), dto.getEndTime());

        // 빌더 패턴을 활용하여 Lecture 객체 생성
        Lecture lecture = Lecture.builder()
                .lecturer(dto.getLecturer())
                .venue(dto.getVenue())
                .capacity(dto.getCapacity())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .description(dto.getDescription())
                .build();
        Lecture saved = lectureRepository.save(lecture);
        log.info("강연 등록 성공: id={}", saved.getId());
        return new LectureResponse(saved);
    }

    // 모든 강연 조회
    @Transactional
    public List<LectureResponse> getAllLectures() {
        List<LectureResponse> dtos = lectureRepository.findAll().stream()
                .map(LectureResponse::new)
                .collect(Collectors.toList());
        log.info("전체 강연 조회: {}건", dtos.size());
        return dtos;
    }
}
