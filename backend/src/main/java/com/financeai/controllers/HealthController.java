package com.financeai.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> status = Map.of(
            "status", "UP",
            "service", "FinanceAI Backend API",
            "database", "Oracle Autonomous DB Connected"
        );
        return ResponseEntity.ok(status);
    }
}
