package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analisis-financiero")
public class AnalisisFinancieroController {
    private final AnalisisFinancieroService service;

    @PostMapping
    public ResponseEntity<?> clasificarTransacciones(@RequestBody SolicitudAnalisisFinancieroDTO dto) {
        // RespuestaAnalisisFinancieroDTO dtoRespuesta = service.clasificarTransacciones(dto);
        Map<String, Double> resumenGastos = Map.of(
            "Alimentación", 35000.0,
            "Servicios", 15000.0,
            "Entretenimiento", 10000.0,
            "Transporte", 8000.0
        );

        RespuestaAnalisisFinancieroDTO dtoRespuesta = new RespuestaAnalisisFinancieroDTO(
            "Moderado",
            0.85,
            resumenGastos,
            "Se sugiere reducir los gastos en entretenimiento y aumentar la reserva de ahorro.",
            "El análisis indica un comportamiento financiero equilibrado con oportunidades de optimización en gastos secundarios."
        );

        return ResponseEntity.ok(dtoRespuesta);
    }
}
