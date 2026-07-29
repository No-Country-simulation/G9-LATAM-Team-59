package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analisis-financiero")
public class AnalisisFinancieroController {
    private final AnalisisFinancieroService service;

    // Ahora se comunicará con el modelo de IA

    @PostMapping
    public ResponseEntity<?> realizarAnalisisFinanciero(@RequestBody SolicitudAnalisisFinancieroDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinanciero(dto);
        // Map<String, Double> resumenGastos = Map.of(
        //     "Alimentación", 35000.0,
        //     "Servicios", 15000.0,
        //     "Entretenimiento", 10000.0,
        //     "Transporte", 8000.0
        // );

        // RespuestaAnalisisFinancieroDTO dtoRespuesta = new RespuestaAnalisisFinancieroDTO(
        //     "Moderado",
        //     Map.of("Saludable", 0.85, "Peligro", 0.15),
        //     resumenGastos,
        //     "Se sugiere reducir los gastos en entretenimiento y aumentar la reserva de ahorro."
        // );

        return ResponseEntity.ok(dtoRespuesta);
    }

    @PostMapping("/mis-transacciones")
    public ResponseEntity<?> realizarAnalisisFinancieroHistorico(@RequestBody SolicitudAnalisisFinancieroHistoricoDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinancieroHistorico(dto);
        // Map<String, Double> resumenGastos = Map.of(
        //     "Alimentación", 35000.0,
        //     "Servicios", 15000.0,
        //     "Entretenimiento", 10000.0,
        //     "Transporte", 8000.0
        // );

        // RespuestaAnalisisFinancieroDTO dtoRespuesta = new RespuestaAnalisisFinancieroDTO(
        //     "Moderado",
        //     Map.of("Saludable", 0.85, "Peligro", 0.15),
        //     resumenGastos,
        //     "Se sugiere reducir los gastos en entretenimiento y aumentar la reserva de ahorro."
        // );

        return ResponseEntity.ok(dtoRespuesta);
    }
}
