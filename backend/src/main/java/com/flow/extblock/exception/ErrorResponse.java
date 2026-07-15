package com.flow.extblock.exception;

import java.util.List;

/**
 * 에러/검증 실패 응답 바디.
 *
 * @param code    {@link ErrorCode}의 코드값
 * @param message 에러 메시지
 * @param errors  필드 단위 검증 실패 상세 목록(없으면 빈 리스트)
 */
public record ErrorResponse(String code, String message, List<FieldErrorDetail> errors) {

    /**
     * 검증 실패한 개별 필드의 상세 정보.
     *
     * @param field  검증에 실패한 필드/프로퍼티 경로
     * @param reason 실패 사유 메시지
     */
    public record FieldErrorDetail(String field, String reason) {
    }

    /**
     * 필드 에러 없이 {@link ErrorCode}만으로 응답 바디를 생성한다.
     *
     * @param errorCode 응답에 담을 에러 코드
     * @return 생성된 응답 바디
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), List.of());
    }

    /**
     * 필드 단위 검증 오류 목록을 포함한 응답 바디를 생성한다.
     *
     * @param errorCode 응답에 담을 에러 코드
     * @param errors    필드별 검증 실패 상세 목록
     * @return 생성된 응답 바디
     */
    public static ErrorResponse of(ErrorCode errorCode, List<FieldErrorDetail> errors) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), errors);
    }
}
