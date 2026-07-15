package com.flow.extblock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 커스텀 확장자 등록 시 "개수 체크 -> 등록" 구간을 직렬화하기 위한 단일 row 락 테이블.
 * 항상 정확히 1개 row(id=1)만 존재하며, {@code SELECT ... FOR UPDATE}로 잠가서 사용한다.
 * PostgreSQL/MySQL 모두 지원하는 표준 잠금 방식이라 DB 종속성이 없다.
 */
@Entity
@Table(name = "custom_extension_lock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomExtensionLock {

    @Id
    private Short id;
}
