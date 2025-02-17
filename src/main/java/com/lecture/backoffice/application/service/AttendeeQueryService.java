package com.lecture.backoffice.application.service;

import com.lecture.backoffice.infrastructure.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 특정 강연의 신청자(사번) 목록 조회를 위한 읽기 전용 서비스
 */
@Service
public class AttendeeQueryService {

    private final ReservationRepository reservationRepository;

    public AttendeeQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public List<String> getAttendeesByLectureId(Long lectureId) {
        return reservationRepository.findEmployeeNumbersByLectureId(lectureId);
    }
}