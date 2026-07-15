package com.flow.extblock.repository;

import com.flow.extblock.entity.CustomExtension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link CustomExtension}에 대한 조회/저장/삭제를 담당하는 Repository.
 */
public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {

    /**
     * 주어진 이름을 가진 커스텀 확장자가 존재하는지 확인한다.
     *
     * @param name 확인할 확장자 이름(소문자)
     * @return 존재 여부
     */
    boolean existsByName(String name);
}
