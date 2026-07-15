package com.flow.extblock.dto.response;

import java.util.List;

/**
 * 고정 확장자 + 커스텀 확장자 전체 조회 응답 DTO.
 *
 * @param fixedExtensions  고정 확장자 목록(id, name, checked)
 * @param customExtensions 커스텀 확장자 목록(id, name)
 */
public record ExtensionsResponse(
        List<FixedExtensionResponse> fixedExtensions,
        List<CustomExtensionResponse> customExtensions
) {
}
