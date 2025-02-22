package com.lecture.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

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

    /**
     * 실패 응답 생성: items는 빈 객체(여기서는 빈 Map)로 반환됩니다.
     *
     * @param status  오류 상태 코드 (예: 400, 500 등)
     * @param message 에러 메시지
     * @return ResponseEntity with CommonResponse containing an empty Map as items
     */
    public static ResponseEntity<CommonResponse<Object>> errorResponse(HttpStatus status, String message) {
        CommonResponse<Object> response = CommonResponse.builder()
                .status(status.value())
                .message(message)
                .items(Collections.emptyMap())  // 빈 Map으로 반환, JSON 직렬화 시 {}로 출력됨
                .build();
        return ResponseEntity.ok(response);
    }
}
