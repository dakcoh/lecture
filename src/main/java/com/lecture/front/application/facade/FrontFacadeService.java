package com.lecture.front.application.facade;

import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.service.LectureQueryService;
import com.lecture.front.application.service.PopularLectureService;
import com.lecture.front.application.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Front 영역 퍼사드 서비스
 *
 * 클라이언트에 단일 인터페이스로
 * 강연 조회, 예약, 신청 내역 조회, 예약 취소, 인기 강연 조회 기능을 제공
 */
@Service
@RequiredArgsConstructor
public class FrontFacadeService {

    private final LectureQueryService lectureQueryService;
    private final ReservationService reservationService;
    private final PopularLectureService popularLectureService;

    /**
     * 신청 가능한 강연 목록 조회
     * (강연 시작 1주일 전부터 강연 시작 1일 후까지 노출)
     *
     * @return 강연 응답 DTO 리스트
     */
    public List<LectureResponse> getAvailableLectures() {
        return lectureQueryService.getAvailableLectures();
    }

    /**
     * 강연 예약 처리
     *
     * @param dto 예약 요청 DTO (강연 ID, 사번)
     * @return 예약 응답 DTO
     */
    public ReservationResponse reserveLecture(ReservationRequest dto) {
        return reservationService.reserve(dto);
    }

    /**
     * 사번을 기준으로 예약 내역 조회
     *
     * @param employeeNumber 사번
     * @return 예약 응답 DTO 리스트
     */
    public List<ReservationResponse> getReservationsByEmployee(String employeeNumber) {
        return reservationService.getReservationsByEmployee(employeeNumber);
    }

    /**
     * 예약 취소 처리
     *
     * @param reservationId 예약 ID
     * @param employeeNumber 사번
     */
    public void cancelReservation(Long reservationId, String employeeNumber) {
        reservationService.cancelReservation(reservationId, employeeNumber);
    }

    /**
     * 최근 3일간 신청 건수가 많은 인기 강연 조회
     *
     * @return 강연 응답 DTO 리스트
     */
    public List<LectureResponse> getPopularLectures() {
        return popularLectureService.getPopularLectures();
    }
}
