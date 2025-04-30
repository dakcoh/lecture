package com.lecture.backoffice.application.service;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import com.lecture.backoffice.application.validator.LectureValidator;
import com.lecture.backoffice.domain.repository.LectureAvailabilityRepository;
import com.lecture.backoffice.domain.repository.backOfficeLectureRepository;
import com.lecture.common.domain.model.Lecture;
import com.lecture.common.domain.model.LectureAvailability;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
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
    private final backOfficeLectureRepository lectureRepository;
    private final LectureAvailabilityRepository lectureAvailabilityRepository;

    public LectureService(backOfficeLectureRepository lectureRepository, LectureAvailabilityRepository lectureAvailabilityRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureAvailabilityRepository = lectureAvailabilityRepository;
    }

    // 강연 등록
    @Transactional
    public LectureResponse createLecture(LectureRequest dto) {
        log.info("강연 등록 요청: {}", dto);
        validateLectureTime(dto);

        Lecture lecture = Lecture.toLecture(dto);
        Lecture savedLecture = saveLecture(lecture);
        createAvailability(savedLecture);

        return LectureResponse.toResponse(savedLecture);
    }

    private void validateLectureTime(LectureRequest dto) {
        LectureValidator.validateTimeRange(dto.getStartTime(), dto.getEndTime());
    }

    private Lecture saveLecture(Lecture lecture) {
        Lecture saved = lectureRepository.save(lecture);
        log.info("강연 등록 성공: id={}", saved.getId());
        return saved;
    }

    private void createAvailability(Lecture savedLecture) {
        LectureAvailability availability = LectureAvailability.builder()
                .lecture(savedLecture)
                .availableSeats(savedLecture.getCapacity())
                .reservedCount(0)
                .build();
        lectureAvailabilityRepository.save(availability);
        log.info("예약 관리 정보 생성 성공: lecture_id={}", availability.getLectureId());
    }

    // 모든 강연 조회
    @Transactional(readOnly = true)
    public List<LectureResponse> getAllLectures() {
        List<LectureResponse> dtos = lectureRepository.findAll().stream()
                .map(LectureResponse::new)
                .collect(Collectors.toList());
        log.info("전체 강연 조회: {}건", dtos.size());
        return dtos;
    }
}