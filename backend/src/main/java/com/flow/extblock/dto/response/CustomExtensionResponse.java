package com.flow.extblock.dto.response;

import com.flow.extblock.entity.CustomExtension;

/**
 * 커스텀 확장자 조회/응답 DTO.
 *
 * @param id   커스텀 확장자 id
 * @param name 확장자 이름
 */
public record CustomExtensionResponse(Long id, String name) {

    /**
     * 엔티티를 응답 DTO로 변환한다.
     *
     * @param entity 변환할 {@link CustomExtension} 엔티티
     * @return 변환된 응답 DTO
     */
    public static CustomExtensionResponse from(CustomExtension entity) {
        return new CustomExtensionResponse(entity.getId(), entity.getName());
    }
}
