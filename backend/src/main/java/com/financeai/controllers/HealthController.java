package com.financeai.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "FinanceAI Backend API");

        try {
            // Ejecuta una consulta de prueba directamente en Oracle DB (DUAL)
            Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            if (result != null && result == 1) {
                response.put("database", Map.of(
                    "status", "UP",
                    "provider", "Oracle Autonomous Database (OCI)",
                    "ping", "OK"
                ));
            } else {
                response.put("database", Map.of("status", "DOWN", "ping", "UNEXPECTED_RESPONSE"));
            }
        } catch (Exception e) {
            response.put("database", Map.of(
                "status", "DOWN",
                "provider", "Oracle Autonomous Database (OCI)",
                "error", e.getMessage() != null ? e.getMessage() : "Connection failed"
            ));
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
