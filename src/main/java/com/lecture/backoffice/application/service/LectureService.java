package com.lecture.backoffice.application.service;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import com.lecture.backoffice.application.validator.LectureValidator;
import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.common.domain.model.Lecture;
import com.lecture.backoffice.domain.repository.LectureRepository;
import com.lecture.common.domain.model.LectureAvailability;
import org.springframework.transaction.annotation.Transactional;
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
    private final LectureAvailabilityRepository lectureAvailabilityRepository;
    private final LectureValidator lectureValidator;

    public LectureService(LectureRepository lectureRepository, LectureAvailabilityRepository lectureAvailabilityRepository, LectureValidator lectureValidator) {
        this.lectureRepository = lectureRepository;
        this.lectureAvailabilityRepository = lectureAvailabilityRepository;
        this.lectureValidator = lectureValidator;
    }

    // 강연 등록
    @Transactional
    public LectureResponse createLecture(LectureRequest dto) {
        log.info("강연 등록 요청: {}", dto);
        // 시간 범위 검증
        lectureValidator.validateTimeRange(dto.getStartTime(), dto.getEndTime());

        // 1. Lecture 객체 생성 (빌더 패턴 사용)
        Lecture lecture = Lecture.builder()
                .lecturer(dto.getLecturer())
                .venue(dto.getVenue())
                .capacity(dto.getCapacity())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .description(dto.getDescription())
                .build();

        // 2. 강연 정보 저장 (Lecture 테이블에 생성)
        Lecture savedLecture = lectureRepository.save(lecture);
        log.info("강연 등록 성공: id={}", savedLecture.getId());

        // 3. LectureAvailability 생성
        LectureAvailability availability = LectureAvailability.builder()
                .lecture(savedLecture)  // 연관관계 설정: MapsId 사용 시, lecture의 id가 자동으로 availability의 id로 설정됨
                .availableSeats(savedLecture.getCapacity()) // 초기 availableSeats는 강연 정원과 동일
                .reservedCount(0)
                .build();

        // 4. lecture_availability 테이블에 저장
        lectureAvailabilityRepository.save(availability);
        log.info("예약 관리 정보 생성 성공: lecture_id={}", availability.getLectureId());

        // 5. LectureResponseDto 생성 후 반환 (강연 정보 포함)
        return new LectureResponse(savedLecture);
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
