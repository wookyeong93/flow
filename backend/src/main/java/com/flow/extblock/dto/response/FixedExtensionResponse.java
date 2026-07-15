package com.flow.extblock.dto.response;

import com.flow.extblock.entity.FixedExtension;

/**
 * 고정 확장자 조회/응답 DTO.
 *
 * @param id      고정 확장자 id
 * @param name    확장자 이름
 * @param checked 차단 체크 여부
 */
public record FixedExtensionResponse(Long id, String name, boolean checked) {

    /**
     * 엔티티를 응답 DTO로 변환한다.
     *
     * @param entity 변환할 {@link FixedExtension} 엔티티
     * @return 변환된 응답 DTO
     */
    public static FixedExtensionResponse from(FixedExtension entity) {
        return new FixedExtensionResponse(entity.getId(), entity.getName(), entity.isChecked());
    }
}
