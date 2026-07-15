package com.flow.extblock.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 컨트롤러 전역에서 발생하는 예외를 잡아 {@link ErrorResponse} 형태로 응답한다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 규칙 위반({@link BusinessException})을 해당 {@link ErrorCode}의 HTTP 상태로 응답한다.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    /**
     * {@code @Valid} 요청 바디 검증 실패({@link MethodArgumentNotValidException})를 400으로 응답한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<ErrorResponse.FieldErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.FieldErrorDetail(
                        fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, errors));
    }

    /**
     * {@code @PathVariable}/{@code @Positive} 등 메서드 파라미터 검증 실패
     * ({@link ConstraintViolationException})를 400으로 응답한다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        List<ErrorResponse.FieldErrorDetail> errors = e.getConstraintViolations().stream()
                .map(violation -> new ErrorResponse.FieldErrorDetail(
                        violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        return ResponseEntity.badRequest().body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, errors));
    }

    /**
     * 위에서 처리되지 않은 예상치 못한 예외를 잡아 500으로 응답한다.
     * 내부 예외 메시지/스택트레이스는 응답에 노출하지 않고 서버 로그에만 남긴다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return ResponseEntity.status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}
