package com.flow.extblock.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 고정 확장자 체크 상태 변경 요청 바디.
 *
 * @param checked 변경할 체크 상태
 */
public record FixedExtensionUpdateRequest(
        @NotNull Boolean checked
) {
}
