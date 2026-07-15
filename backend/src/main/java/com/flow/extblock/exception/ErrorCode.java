package com.flow.extblock.exception;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드가 갖춰야 할 공통 계약. 도메인별로 별도 enum(예: {@link CommonErrorCode},
 * {@link ExtensionErrorCode})을 만들어 구현하며, {@link BusinessException}/
 * {@link GlobalExceptionHandler}/{@link ErrorResponse}는 이 인터페이스 타입으로만 다룬다.
 */
public interface ErrorCode {

    HttpStatus getStatus();

    String getCode();

    String getMessage();
}
