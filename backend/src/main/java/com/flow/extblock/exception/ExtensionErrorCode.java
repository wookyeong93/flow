package com.flow.extblock.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 확장자(고정/커스텀) 도메인에서 발생하는 비즈니스 에러 코드.
 */
@Getter
@RequiredArgsConstructor
public enum ExtensionErrorCode implements ErrorCode {

    EXTENSION_NAME_DUPLICATE(HttpStatus.CONFLICT, "EXT001", "이미 등록된 확장자입니다."),
    CUSTOM_EXTENSION_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "EXT002", "커스텀 확장자는 최대 200개까지 등록할 수 있습니다."),
    FIXED_EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "EXT003", "고정 확장자를 찾을 수 없습니다."),
    CUSTOM_EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "EXT004", "커스텀 확장자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
