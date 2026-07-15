package com.flow.extblock.controller;

import com.flow.extblock.dto.request.CustomExtensionCreateRequest;
import com.flow.extblock.dto.request.FixedExtensionUpdateRequest;
import com.flow.extblock.dto.response.CustomExtensionResponse;
import com.flow.extblock.dto.response.ExtensionsResponse;
import com.flow.extblock.dto.response.FixedExtensionResponse;
import com.flow.extblock.service.CustomExtensionService;
import com.flow.extblock.service.FixedExtensionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 고정/커스텀 확장자 조회, 고정 확장자 체크 상태 변경, 커스텀 확장자 생성/삭제 API.
 */
@Validated
@RestController
@RequestMapping("/api/extensions")
@RequiredArgsConstructor
public class ExtensionController {

    private final FixedExtensionService fixedExtensionService;
    private final CustomExtensionService customExtensionService;

    /**
     * 고정 확장자와 커스텀 확장자 전체 목록을 함께 조회한다.
     *
     * @return 고정/커스텀 확장자 목록
     */
    @GetMapping
    public ExtensionsResponse getExtensions() {
        return new ExtensionsResponse(fixedExtensionService.getAll(), customExtensionService.getAll());
    }

    /**
     * 고정 확장자의 체크(차단) 상태를 변경한다.
     *
     * @param id      변경할 고정 확장자 id(경로 변수)
     * @param request 변경할 체크 상태를 담은 요청 바디
     * @return 변경된 고정 확장자
     */
    @PatchMapping("/fixed/{id}")
    public FixedExtensionResponse updateFixedExtension(
            @PathVariable @Positive Long id,
            @Valid @RequestBody FixedExtensionUpdateRequest request) {
        return fixedExtensionService.updateChecked(id, request.checked());
    }

    /**
     * 커스텀 확장자를 신규 등록한다.
     *
     * @param request 등록할 확장자 이름을 담은 요청 바디
     * @return 생성된 커스텀 확장자
     */
    @PostMapping("/custom")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomExtensionResponse createCustomExtension(@Valid @RequestBody CustomExtensionCreateRequest request) {
        return customExtensionService.create(request.name());
    }

    /**
     * 커스텀 확장자를 삭제한다.
     *
     * @param id 삭제할 커스텀 확장자 id(경로 변수)
     */
    @DeleteMapping("/custom/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomExtension(@PathVariable @Positive Long id) {
        customExtensionService.delete(id);
    }
}
