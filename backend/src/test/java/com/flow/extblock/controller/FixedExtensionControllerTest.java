package com.flow.extblock.controller;

import com.flow.extblock.entity.FixedExtension;
import com.flow.extblock.repository.FixedExtensionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 고정 확장자 API 통합 테스트. 로컬 Postgres(docker compose)를 그대로 사용하며,
 * 각 테스트는 {@code @Transactional}로 종료 후 롤백되어 데이터가 남지 않는다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FixedExtensionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FixedExtensionRepository fixedExtensionRepository;

    @Test
    @DisplayName("정상값: 체크 상태를 변경하면 조회 결과에도 반영된다")
    void updateChecked_reflectsInList() throws Exception {
        FixedExtension target = fixedExtensionRepository.findAll().get(0);

        mockMvc.perform(patch("/api/extensions/fixed/{id}", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"checked\":true}"))
                .andExpect(status().isOk());

        String body = mockMvc.perform(get("/api/extensions"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode fixedExtensions = objectMapper.readTree(body).get("fixedExtensions");
        boolean checked = false;
        for (JsonNode node : fixedExtensions) {
            if (node.get("id").asLong() == target.getId()) {
                checked = node.get("checked").asBoolean();
            }
        }
        assertTrue(checked);
    }

    @Test
    @DisplayName("경계값: id가 0 이하이면 400")
    void updateChecked_nonPositiveId_returns400() throws Exception {
        mockMvc.perform(patch("/api/extensions/fixed/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"checked\":true}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/extensions/fixed/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"checked\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("경계값: 존재하지 않는 id면 404")
    void updateChecked_notFoundId_returns404() throws Exception {
        mockMvc.perform(patch("/api/extensions/fixed/{id}", 999_999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"checked\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("벨리데이션 오류값: checked 필드가 없으면 400")
    void updateChecked_missingCheckedField_returns400() throws Exception {
        FixedExtension target = fixedExtensionRepository.findAll().get(0);

        mockMvc.perform(patch("/api/extensions/fixed/{id}", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
