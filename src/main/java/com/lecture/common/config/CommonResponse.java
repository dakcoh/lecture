package com.lecture.common.config;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공통 응답 DTO
 * - status: HTTP 상태 코드
 * - message: 응답 메시지
 * - items: 실제 데이터 (제네릭)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"status", "message", "items"})
public class CommonResponse<T> {
    private int status;
    private String message;
    private T items;
}