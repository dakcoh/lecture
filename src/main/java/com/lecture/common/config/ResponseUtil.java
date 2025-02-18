package com.lecture.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 공통 응답 생성 utils
 */
public class ResponseUtil {

    /**
     * 성공 응답 생성 (사용자 정의 상태와 메시지)
     *
     * @param status  HTTP 상태 코드
     * @param message 응답 메시지
     * @param items   반환할 데이터
     * @param <T>     데이터 타입
     * @return ResponseEntity with CommonResponse
     */
    public static <T> ResponseEntity<CommonResponse<T>> successResponse(HttpStatus status, String message, T items) {
        CommonResponse<T> response = CommonResponse.<T>builder()
                .status(status.value())
                .message(message)
                .items(items)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
