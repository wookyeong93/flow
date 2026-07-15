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
 * 자주 차단하는 고정 확장자(bat, cmd, com, cpl, exe, scr, js) 엔티티.
 * 목록은 Flyway 시드 데이터로 고정되며, {@link #checked} 상태만 변경 가능하다.
 */
@Entity
@Table(name = "fixed_extension")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false)
    private boolean checked;

    /**
     * 차단 여부 체크 상태를 변경한다.
     *
     * @param checked 변경할 체크 상태
     */
    public void changeChecked(boolean checked) {
        this.checked = checked;
    }
}
