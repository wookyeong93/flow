package com.flow.extblock.repository;

import com.flow.extblock.entity.FixedExtension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link FixedExtension}에 대한 조회/저장을 담당하는 Repository.
 */
public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long> {

    /**
     * 주어진 이름을 가진 고정 확장자가 존재하는지 확인한다.
     * 커스텀 확장자 등록 시 교차 중복 체크에 사용된다.
     *
     * @param name 확인할 확장자 이름(소문자)
     * @return 존재 여부
     */
    boolean existsByName(String name);
}
