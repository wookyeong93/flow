package com.flow.extblock.exception;

/**
 * 비즈니스 규칙 위반 시 던지는 예외들의 공통 상위 클래스.
 * 도메인마다 이 클래스를 상속하는 구체 예외 클래스(예: {@link ExtensionException})를 두고,
 * {@link #getErrorCode()}로 종류를 구분한다. {@link GlobalExceptionHandler}는 이 클래스
 * 하나로 공통 처리한다.
 *
 * <p>참고: Spring의 {@code @ExceptionHandler}는 {@code Class<? extends Throwable>}만 받을 수
 * 있어 인터페이스로는 선언할 수 없다({@code interface}는 {@code Throwable}을 extends할 수 없음).
 * 그래서 도메인별 유연한 확장은 인터페이스가 아니라 이 추상 클래스 상속으로 구현한다.</p>
 */
public abstract class BusinessException extends RuntimeException {

    protected BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public abstract ErrorCode getErrorCode();
}
