package com.lecture.front.application.validate;

import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.domain.model.Lecture;
import com.lecture.front.domain.model.Reservation;
import com.lecture.front.domain.repository.LectureRepository;
import com.lecture.front.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 강연 예약 요청에 대한 유효성 검증 로직을 분리한 Validator 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationValidator {

    @Qualifier("frontLectureRepository")
    private final LectureRepository lectureRepository;
    @Qualifier("frontReservationRepository")
    private final ReservationRepository reservationRepository;

    /**
     * 예약 요청에 대해 다음을 검증합니다.
     * 1. 강연 존재 여부
     * 2. 같은 강연에 대해 중복 예약이 없는지
     * 3. 강연 정원이 초과되지 않았는지
     * 4. 같은 사번으로 이미 예약된 강연 중, 시간 겹침이 없는지
     *
     * @param dto 예약 요청 DTO
     * @return 유효한 Lecture 엔티티 (예약하려는 강연)
     * @throws IllegalArgumentException 검증 실패 시 예외 발생
     */
    public Lecture validateReservation(ReservationRequest dto) {
        // 1. 강연 존재 확인 및 비관적 락 적용 (동일 강연에 대한 동시 변경 방지)
        Lecture lecture = lectureRepository.findByIdWithLock(dto.getLectureId());
        if (lecture == null) {
            log.error("강연 신청 실패: 존재하지 않는 강연입니다. lectureId={}", dto.getLectureId());
            throw new IllegalArgumentException("존재하지 않는 강연입니다.");
        }

        // 2. 동일 강연 중복 예약 검증
        if (reservationRepository.existsByLectureIdAndEmployeeNumber(dto.getLectureId(), dto.getEmployeeNumber())) {
            log.error("예약 실패: 중복 예약. lectureId={}, employeeNumber={}",
                    dto.getLectureId(), dto.getEmployeeNumber());
            throw new IllegalArgumentException("이미 신청한 강연입니다.");
        }

        // 3. 강연 정원 초과 검증
        long currentReservations = reservationRepository.countByLectureId(dto.getLectureId());
        if (currentReservations >= lecture.getCapacity()) {
            log.error("예약 실패: 강연 정원 초과. lectureId={}, 현재예약수={}, 정원={}",
                    dto.getLectureId(), currentReservations, lecture.getCapacity());
            throw new IllegalArgumentException("강연 정원이 초과되었습니다.");
        }

        // 4. 동일 시간대 중복 예약 검증
        List<Reservation> existingReservations = reservationRepository.findByEmployeeNumber(dto.getEmployeeNumber());
        for (Reservation existing : existingReservations) {
            Lecture reservedLecture = lectureRepository.findById(existing.getLectureId());
            if (reservedLecture != null && isOverlapping(reservedLecture, lecture)) {
                log.error("예약 실패: 같은 시간대에 이미 예약된 강연 존재. employeeNumber={}, 기존강연ID={}, 새강연ID={}",
                        dto.getEmployeeNumber(), reservedLecture.getId(), lecture.getId());
                throw new IllegalArgumentException("이미 같은 시간에 예약된 강연이 있습니다.");
            }
        }

        return lecture;
    }

    /**
     * 두 강연의 시간대가 겹치는지 확인하는 헬퍼 메서드
     */
    private boolean isOverlapping(Lecture lecture1, Lecture lecture2) {
        return lecture1.getStartTime().isBefore(lecture2.getEndTime()) &&
                lecture2.getStartTime().isBefore(lecture1.getEndTime());
    }
}
