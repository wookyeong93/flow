package com.flow.extblock.service;

import com.flow.extblock.dto.response.CustomExtensionResponse;
import com.flow.extblock.entity.CustomExtension;
import com.flow.extblock.exception.ExtensionErrorCode;
import com.flow.extblock.exception.ExtensionException;
import com.flow.extblock.repository.CustomExtensionLockRepository;
import com.flow.extblock.repository.CustomExtensionRepository;
import com.flow.extblock.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 커스텀 확장자 조회, 등록, 삭제를 담당하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class CustomExtensionService {

    private static final int MAX_CUSTOM_EXTENSION_COUNT = 200;
    private static final short LOCK_ROW_ID = 1;

    private final CustomExtensionRepository customExtensionRepository;
    private final FixedExtensionRepository fixedExtensionRepository;
    private final CustomExtensionLockRepository customExtensionLockRepository;

    /**
     * 커스텀 확장자 전체 목록을 조회한다.
     *
     * @return 커스텀 확장자 응답 목록
     */
    @Transactional(readOnly = true)
    public List<CustomExtensionResponse> getAll() {
        return customExtensionRepository.findAll().stream()
                .map(CustomExtensionResponse::from)
                .toList();
    }

    /**
     * 커스텀 확장자를 등록한다. 이름은 소문자로 정규화한 뒤 저장하며,
     * 고정/커스텀 확장자 전체와의 대소문자 무시 중복, 최대 개수(200개)를 검사한다.
     *
     * 개수 체크와 등록 사이에 동시 요청이 끼어들어 200개를 초과하는 것을 막기 위해,
     * {@link CustomExtensionLockRepository}로 락 row를 {@code SELECT ... FOR UPDATE} 잠금 후 진행한다
     * (PostgreSQL/MySQL 모두 지원하는 표준 잠금 방식).
     *
     * @param rawName 등록할 확장자 이름(정규화 전 원본 입력값)
     * @return 등록된 커스텀 확장자 응답
     * @throws ExtensionException 이름이 중복되면 {@link ExtensionErrorCode#EXTENSION_NAME_DUPLICATE},
     *                             최대 개수를 초과하면 {@link ExtensionErrorCode#CUSTOM_EXTENSION_LIMIT_EXCEEDED}
     */
    @Transactional
    public CustomExtensionResponse create(String rawName) {
        customExtensionLockRepository.findById(LOCK_ROW_ID);

        String name = rawName.toLowerCase();

        if (fixedExtensionRepository.existsByName(name) || customExtensionRepository.existsByName(name)) {
            throw new ExtensionException(ExtensionErrorCode.EXTENSION_NAME_DUPLICATE);
        }
        if (customExtensionRepository.count() >= MAX_CUSTOM_EXTENSION_COUNT) {
            throw new ExtensionException(ExtensionErrorCode.CUSTOM_EXTENSION_LIMIT_EXCEEDED);
        }

        CustomExtension saved = customExtensionRepository.save(new CustomExtension(name));
        return CustomExtensionResponse.from(saved);
    }

    /**
     * 커스텀 확장자를 삭제한다.
     *
     * @param id 삭제할 커스텀 확장자 id
     * @throws ExtensionException id에 해당하는 커스텀 확장자가 없으면 {@link ExtensionErrorCode#CUSTOM_EXTENSION_NOT_FOUND}
     */
    @Transactional
    public void delete(Long id) {
        if (!customExtensionRepository.existsById(id)) {
            throw new ExtensionException(ExtensionErrorCode.CUSTOM_EXTENSION_NOT_FOUND);
        }
        customExtensionRepository.deleteById(id);
    }
}
