package com.lecture.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기
 * Validator에서 발생한 예외를 공통 응답(CommonResponse)으로 변환하여 반환합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("예외 발생: {}", ex.getMessage(), ex);
        // 실패 응답 생성: 내부적으로 status 필드에 오류 상태(예: 400)를 포함합니다.
        return ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<CommonResponse<Object>> handleNumberFormatException(NumberFormatException ex) {
        log.error("NumberFormatException 발생: {}", ex.getMessage(), ex);
        return ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "입력된 값이 올바른 숫자 형식이 아닙니다.");
    }
}
