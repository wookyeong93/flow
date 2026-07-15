package com.flow.extblock.repository;

import com.flow.extblock.entity.CustomExtensionLock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

/**
 * {@link CustomExtensionLock}(단일 row 락)에 대한 Repository.
 */
public interface CustomExtensionLockRepository extends JpaRepository<CustomExtensionLock, Short> {

    /**
     * 락 row를 {@code SELECT ... FOR UPDATE}로 잠근 채로 조회한다.
     *
     * @param id 락 row id(항상 1)
     * @return 락 row
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CustomExtensionLock> findById(Short id);
}
