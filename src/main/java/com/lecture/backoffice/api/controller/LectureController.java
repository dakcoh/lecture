package com.lecture.backoffice.api.controller;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import com.lecture.backoffice.application.service.LectureService;
import com.lecture.common.config.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 강연 관련 API 엔드포인트
 */
@RestController("backofficeLectureController")
@RequestMapping("/api/backoffice/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // 강연 등록 API
    @PostMapping
    public ResponseEntity<CommonResponse<LectureResponse>> createLecture(@RequestBody @Valid LectureRequest dto) {
        LectureResponse response = lectureService.createLecture(dto);
        return ResponseUtil.successResponse(HttpStatus.OK, "강연 저장 성공", response);
    }

    // 전체 강연 조회 API
    @GetMapping
    public ResponseEntity<CommonResponse<List<LectureResponse>>> getAllLectures() {
        List<LectureResponse> lectures = lectureService.getAllLectures();
        return ResponseUtil.successResponse(HttpStatus.OK, "전체 강연 조회 성공", lectures);
    }
}