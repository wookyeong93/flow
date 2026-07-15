package com.flow.extblock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 직접 등록하는 커스텀 확장자 엔티티.
 * 생성/삭제만 가능하며 수정은 지원하지 않는다.
 */
@Entity
@Table(name = "custom_extension")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    /**
     * @param name 등록할 확장자 이름(소문자로 정규화된 값이어야 한다)
     */
    public CustomExtension(String name) {
        this.name = name;
    }
}
