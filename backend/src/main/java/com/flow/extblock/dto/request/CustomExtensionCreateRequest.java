package com.flow.extblock.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 커스텀 확장자 등록 요청 바디.
 *
 * @param name 등록할 확장자 이름(영문으로 시작, 이후 영문/숫자 허용, 최대 20자)
 */
public record CustomExtensionCreateRequest(

        @NotBlank
        @Size(max = 20)
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z0-9]*$",
                message = "확장자는 영문으로 시작해야 하며 영문/숫자만 입력 가능합니다."
        )
        String name
) {
}
