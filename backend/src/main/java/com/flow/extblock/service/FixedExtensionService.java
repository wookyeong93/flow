package com.flow.extblock.service;

import com.flow.extblock.dto.response.FixedExtensionResponse;
import com.flow.extblock.entity.FixedExtension;
import com.flow.extblock.exception.ExtensionErrorCode;
import com.flow.extblock.exception.ExtensionException;
import com.flow.extblock.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 고정 확장자 조회 및 체크 상태 변경을 담당하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class FixedExtensionService {

    private final FixedExtensionRepository fixedExtensionRepository;

    /**
     * 고정 확장자 전체 목록을 조회한다.
     *
     * @return 고정 확장자 응답 목록
     */
    @Transactional(readOnly = true)
    public List<FixedExtensionResponse> getAll() {
        return fixedExtensionRepository.findAllByOrderByIdAsc().stream()
                .map(FixedExtensionResponse::from)
                .toList();
    }

    /**
     * 고정 확장자의 체크 상태를 변경한다.
     *
     * @param id      변경할 고정 확장자 id
     * @param checked 변경할 체크 상태
     * @return 변경된 고정 확장자 응답
     * @throws ExtensionException id에 해당하는 고정 확장자가 없으면 {@link ExtensionErrorCode#FIXED_EXTENSION_NOT_FOUND}
     */
    @Transactional
    public FixedExtensionResponse updateChecked(Long id, boolean checked) {
        FixedExtension fixedExtension = fixedExtensionRepository.findById(id)
                .orElseThrow(() -> new ExtensionException(ExtensionErrorCode.FIXED_EXTENSION_NOT_FOUND));
        fixedExtension.changeChecked(checked);
        return FixedExtensionResponse.from(fixedExtension);
    }
}
