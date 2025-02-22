package com.lecture.front.api.controller;

import com.lecture.common.config.CommonResponse;
import com.lecture.common.config.ResponseUtil;
import com.lecture.front.api.dto.LectureResponse;
import com.lecture.front.application.facade.FrontFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 강연 조회 관련 API
 * - 신청 가능한 강연 목록 조회 (강연 시작 1주일 전 ~ 1일 후)
 * - 실시간 인기 강연 조회 (최근 3일간 신청 건수 기준)
 */
@RestController("frontLectureController")
@RequestMapping("/api/front/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final FrontFacadeService facadeService;

    // 신청 가능한 강연 목록 조회
    @GetMapping("/available")
    public ResponseEntity<CommonResponse<List<LectureResponse>>> getAvailableLectures() {
        List<LectureResponse> lectures = facadeService.getAvailableLectures();
        return ResponseUtil.successResponse(HttpStatus.OK, "신청 가능한 강연 목록 조회 성공", lectures);
    }

    // 실시간 인기 강연 조회
    @GetMapping("/popular")
    public ResponseEntity<CommonResponse<List<LectureResponse>>> getPopularLectures() {
        List<LectureResponse> popularLectures = facadeService.getPopularLectures();
        return ResponseUtil.successResponse(HttpStatus.OK, "실시간 인기 강연 조회 성공", popularLectures);
    }
}
