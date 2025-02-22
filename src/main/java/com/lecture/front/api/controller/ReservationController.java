package com.lecture.front.api.controller;

import com.lecture.common.config.CommonResponse;
import com.lecture.common.config.ResponseUtil;
import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.front.application.facade.FrontFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 예약 관련 API
 * - 강연 신청 (예약 생성)
 * - 사번으로 신청 내역 조회
 * - 예약 취소
 */
@RestController
@RequestMapping("/api/front/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final FrontFacadeService facadeService;

    // 강연 신청: 사번과 강연 ID를 포함하는 요청
    @PostMapping
    public ResponseEntity<CommonResponse<ReservationResponse>> reserveLecture(@RequestBody ReservationRequest dto) {
        ReservationResponse response = facadeService.reserveLecture(dto);
        return ResponseUtil.successResponse(HttpStatus.OK, "강연 신청 성공", response);
    }

    // 사번으로 신청 내역 조회
    @GetMapping("/{employeeNumber}")
    public ResponseEntity<CommonResponse<List<ReservationResponse>>> getReservationsByEmployee(@PathVariable String employeeNumber) {
        List<ReservationResponse> reservations = facadeService.getReservationsByEmployee(employeeNumber);
        return ResponseUtil.successResponse(HttpStatus.OK, "사번으로 강연 조회 성공", reservations);
    }

    // 신청한 강연 취소
    @DeleteMapping("/reservations/{reservationId}/employee/{employeeNumber}")
    public ResponseEntity<CommonResponse<Object>> cancelReservation(@PathVariable Long reservationId, @PathVariable String employeeNumber) {
        facadeService.cancelReservation(reservationId, employeeNumber);
        return ResponseUtil.successResponse(HttpStatus.OK, "실시간 강연 취소 성공", null);
    }

}
