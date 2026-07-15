package com.flow.extblock.exception;

import lombok.Getter;

/**
 * 확장자(고정/커스텀) 도메인에서 발생하는 비즈니스 예외.
 */
@Getter
public class ExtensionException extends BusinessException {

    private final ErrorCode errorCode;

    /**
     * @param errorCode 발생한 에러의 종류를 나타내는 코드
     */
    public ExtensionException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
