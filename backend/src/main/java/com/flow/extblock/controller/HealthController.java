package com.flow.extblock.controller;

import com.flow.extblock.dto.response.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버 연결 확인용 헬스 체크 API.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * 서버가 정상적으로 응답 가능한 상태인지 확인한다.
     *
     * @return 서버 상태 응답
     */
    @GetMapping
    public HealthResponse check() {
        return new HealthResponse("UP");
    }
}
