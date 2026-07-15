package com.flow.extblock.dto.response;

/**
 * 서버 상태 확인 응답 DTO.
 *
 * @param status 서버 상태 문자열
 */
public record HealthResponse(String status) {
}
