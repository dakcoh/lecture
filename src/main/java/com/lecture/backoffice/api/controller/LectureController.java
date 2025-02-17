package com.lecture.backoffice.api.controller;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import com.lecture.backoffice.application.service.LectureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 강연 관련 API 엔드포인트
 */
@RestController
@RequestMapping("/api/backoffice/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // 강연 등록 API
    @PostMapping
    public ResponseEntity<LectureResponse> createLecture(@RequestBody @Valid LectureRequest dto) {
        LectureResponse response = lectureService.createLecture(dto);
        return ResponseEntity.ok(response);
    }

    // 전체 강연 조회 API
    @GetMapping
    public ResponseEntity<List<LectureResponse>> getAllLectures() {
        List<LectureResponse> lectures = lectureService.getAllLectures();
        return ResponseEntity.ok(lectures);
    }
}