package com.lecture.backoffice.application.service;

import com.lecture.backoffice.domain.repository.backOfficeReservationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 특정 강연의 신청자(사번) 목록 조회를 위한 읽기 전용 서비스
 */
@Service
public class AttendeeQueryService {
    private final backOfficeReservationRepository backOfficeReservationRepository;

    public AttendeeQueryService(backOfficeReservationRepository backOfficeReservationRepository) {
        this.backOfficeReservationRepository = backOfficeReservationRepository;
    }

    @Transactional(readOnly = true)
    public List<String> getAttendeesByLectureId(Long lectureId) {
        return backOfficeReservationRepository.findEmployeeNumbersByLectureId(lectureId);
    }
}