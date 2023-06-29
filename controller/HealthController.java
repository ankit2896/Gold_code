package com.freecharge.financial.controller;

import com.freecharge.financial.dto.response.HealthResponse;
import com.freecharge.financial.service.HealthIndicatoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/gold")
@Tag(name = "Gold health")
public class HealthController {

    @Autowired
    public HealthIndicatoreService healthCheckService;

    @Operation(summary = "Verify gold server is live or not")
    @GetMapping(value = "/health")
    public String testingAPI() {
        return "OK 200";
    }

    @Operation(summary = "Verify gold deep-health or not")
    @GetMapping(value = "/deep-health")
    public ResponseEntity<HealthResponse> getDeepHealth(
            HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) {
        return healthCheckService.getDeepHealth();
    }
}
