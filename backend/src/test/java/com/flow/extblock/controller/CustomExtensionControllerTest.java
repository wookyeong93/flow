package com.flow.extblock.controller;

import com.flow.extblock.dto.request.CustomExtensionCreateRequest;
import com.flow.extblock.entity.CustomExtension;
import com.flow.extblock.repository.CustomExtensionRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 커스텀 확장자 API 통합 테스트. 로컬 Postgres(docker compose)를 그대로 사용하며,
 * 각 테스트는 {@code @Transactional}로 종료 후 롤백되어 데이터가 남지 않는다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomExtensionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomExtensionRepository customExtensionRepository;

    @Autowired
    private FixedExtensionRepository fixedExtensionRepository;

    private String createRequestJson(String name) throws Exception {
        return objectMapper.writeValueAsString(new CustomExtensionCreateRequest(name));
    }

    // ---------- 1. 정상값 ----------

    @Test
    @DisplayName("정상값: 등록 -> 조회 -> 삭제 흐름")
    void createListDelete_happyPath() throws Exception {
        String body = mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("zippy")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode created = objectMapper.readTree(body);
        assertEquals("zippy", created.get("name").asString());
        long id = created.get("id").asLong();

        String listBody = mockMvc.perform(get("/api/extensions"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode customExtensions = objectMapper.readTree(listBody).get("customExtensions");
        boolean found = false;
        for (JsonNode node : customExtensions) {
            if (node.get("name").asString().equals("zippy")) {
                found = true;
            }
        }
        assertTrue(found);

        mockMvc.perform(delete("/api/extensions/custom/{id}", id))
                .andExpect(status().isNoContent());
        assertFalse(customExtensionRepository.existsById(id));
    }

    // ---------- 2. 경계값 ----------

    @Test
    @DisplayName("경계값: 이름 길이 1자/20자는 허용, 21자는 거부")
    void create_nameLengthBoundary() throws Exception {
        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("a")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("a".repeat(20))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("a".repeat(21))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("경계값: 정확히 200개까지 등록 가능하고 201번째는 거부된다")
    void create_countBoundary() throws Exception {
        long currentCount = customExtensionRepository.count();
        List<CustomExtension> fillers = new ArrayList<>();
        for (long i = currentCount; i < 199; i++) {
            fillers.add(new CustomExtension("filler" + i));
        }
        customExtensionRepository.saveAll(fillers);
        assertEquals(199, customExtensionRepository.count());

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("okname")))
                .andExpect(status().isCreated());
        assertEquals(200, customExtensionRepository.count());

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("overlimit")))
                .andExpect(status().isConflict());
        assertEquals(200, customExtensionRepository.count());
    }

    @Test
    @DisplayName("경계값: id가 0 이하면 400, 존재하지 않으면 404")
    void delete_idBoundary() throws Exception {
        mockMvc.perform(delete("/api/extensions/custom/{id}", 0))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/extensions/custom/{id}", -1))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/extensions/custom/{id}", 999_999_999L))
                .andExpect(status().isNotFound());
    }

    // ---------- 3. 벨리데이션 오류값 ----------

    @Test
    @DisplayName("벨리데이션 오류값: 숫자만 입력하면 거부")
    void create_digitsOnly_rejected() throws Exception {
        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("벨리데이션 오류값: 숫자로 시작하면 거부")
    void create_startsWithDigit_rejected() throws Exception {
        for (String invalid : List.of("1abc", "9zip")) {
            mockMvc.perform(post("/api/extensions/custom")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("벨리데이션 오류값: 콤마/점/공백 등 다중 확장자 형태는 거부")
    void create_disallowedCharacters_rejected() throws Exception {
        for (String invalid : List.of("exe,js", "e.xe", "ex e")) {
            mockMvc.perform(post("/api/extensions/custom")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("벨리데이션 오류값: 빈 문자열은 거부")
    void create_blank_rejected() throws Exception {
        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("벨리데이션: 대문자 입력은 소문자로 정규화되어 저장된다")
    void create_uppercaseNormalizedToLowercase() throws Exception {
        String body = mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("ZipUp")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals("zipup", objectMapper.readTree(body).get("name").asString());
    }

    @Test
    @DisplayName("벨리데이션: 고정 확장자와 대소문자 무시 중복이면 거부")
    void create_duplicateWithFixedExtension_rejected() throws Exception {
        String fixedName = fixedExtensionRepository.findAll().get(0).getName();

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson(fixedName.toUpperCase())))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("벨리데이션: 이미 등록된 커스텀 확장자와 대소문자 무시 중복이면 거부")
    void create_duplicateWithCustomExtension_rejected() throws Exception {
        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("dupname")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson("DUPNAME")))
                .andExpect(status().isConflict());
    }

    // ---------- 4. SQL Injection 시도 ----------

    @Test
    @DisplayName("SQL Injection: 페이로드는 형식 검증 단계에서 차단되고 테이블도 무사하다")
    void create_sqlInjectionPayload_rejected() throws Exception {
        for (String payload : List.of("'; DROP TABLE custom_extension; --", "1' OR '1'='1")) {
            mockMvc.perform(post("/api/extensions/custom")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson(payload)))
                    .andExpect(status().isBadRequest());
        }
        assertDoesNotThrow(() -> customExtensionRepository.count());
    }

    // ---------- 5. XSS 시도 ----------

    @Test
    @DisplayName("XSS: 스크립트 페이로드는 형식 검증 단계에서 차단된다")
    void create_xssPayload_rejected() throws Exception {
        for (String payload : List.of("<script>alert(1)</script>", "<img src=x onerror=alert(1)>")) {
            mockMvc.perform(post("/api/extensions/custom")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson(payload)))
                    .andExpect(status().isBadRequest());
        }
    }
}
