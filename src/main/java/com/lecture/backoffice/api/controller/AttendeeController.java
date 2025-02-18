package com.lecture.backoffice.api.controller;

import com.lecture.backoffice.application.service.AttendeeQueryService;
import com.lecture.common.config.CommonResponse;
import com.lecture.common.config.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 특정 강연의 신청자(사번) 목록을 조회하는 API 엔드포인트
 */
@RestController
@RequestMapping("/api/backoffice/lectures")
public class AttendeeController {

    private final AttendeeQueryService attendeeQueryService;

    public AttendeeController(AttendeeQueryService attendeeQueryService) {
        this.attendeeQueryService = attendeeQueryService;
    }

    // 강연의 신청자 사번 목록 조회 API
    @GetMapping("/{lectureId}/attendees")
    public ResponseEntity<CommonResponse<List<String>>> getAttendeeList(@PathVariable Long lectureId) {
        List<String> attendees = attendeeQueryService.getAttendeesByLectureId(lectureId);
        return ResponseUtil.successResponse(HttpStatus.OK, "강연 신청자 사번 목록 조회 성공", attendees);
    }
}
