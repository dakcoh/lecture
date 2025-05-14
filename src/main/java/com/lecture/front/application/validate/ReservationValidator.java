package com.lecture.front.application.validate;

import com.lecture.common.domain.model.ReservationStatus;
import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.common.domain.model.Lecture;
import com.lecture.front.domain.repository.ReservationRepository;
import com.lecture.front.domain.repository.FrontLectureQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 강연 예약 요청에 대한 유효성 검증 로직을 분리한 Validator 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationValidator {
    private final FrontLectureQueryRepository lectureRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 예약 요청에 대해 다음을 검증합니다.
     * 1. 강연 존재 여부
     * 2. 같은 강연에 대해 중복 예약이 없는지
     * 3. 강연 정원이 초과되지 않았는지
     * 4. 같은 사번으로 이미 예약된 강연 중, 시간 겹침이 없는지
     */
    public Lecture validateReservation(ReservationRequest dto) {
        Lecture lecture = validateLectureExistence(dto.getLectureId());
        validateDuplicateReservation(dto.getLectureId(), dto.getEmployeeNumber());
        validateCapacity(lecture, dto.getLectureId());

        return lecture;
    }

    private Lecture validateLectureExistence(Long lectureId) {
        Lecture lecture = lectureRepository.findByIdWithLock(lectureId);
        if (lecture == null) {
            log.error("강연 신청 실패: 존재하지 않는 강연입니다. lectureId={}", lectureId);
            throw new IllegalArgumentException("존재하지 않는 강연입니다.");
        }
        return lecture;
    }

    private void validateDuplicateReservation(Long lectureId, String employeeNumber) {
        if (reservationRepository.existsByLectureIdAndEmployeeNumberAndStatus(lectureId, employeeNumber, ReservationStatus.CONFIRMED)) {
            log.error("예약 실패: 이미 활성 예약이 존재합니다. lectureId={}, employeeNumber={}", lectureId, employeeNumber);
            throw new IllegalArgumentException("이미 신청한 강연입니다.");
        }
    }

    private void validateCapacity(Lecture lecture, Long lectureId) {
        long currentReservations = reservationRepository.countByLectureIdAndStatus(lectureId, ReservationStatus.CONFIRMED);
        if (currentReservations >= lecture.getCapacity()) {
            log.error("예약 실패: 강연 정원 초과. lectureId={}, 현재예약수={}, 정원={}", lectureId, currentReservations, lecture.getCapacity());
            throw new IllegalArgumentException("강연 정원이 초과되었습니다.");
        }
    }
}
