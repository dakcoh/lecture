package com.lecture.backoffice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 특정 강연의 신청자 사번 목록 응답 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class AttendeeListResponse {
    private List<String> employeeNumbers;
}
