package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    //  Map<String, Double> resumenGastos = Map.of(
    //      "Alimentación", 35000.0,
    // "Servicios", 15000.0,
    //   "Entretenimiento", 10000.0,
    //      "Transporte", 8000.0
    //  );

    //  RespuestaAnalisisFinancieroDTO dtoRespuesta = new RespuestaAnalisisFinancieroDTO(
    //      "Moderado",
    //     Map.of("Saludable", 0.85, "Peligro", 0.15),
    //      resumenGastos,
    //      "Se sugiere reducir los gastos en entretenimiento y aumentar la reserva de ahorro."
    // );

         return ResponseEntity.ok(dtoRespuesta);
     }

    @PostMapping("/historico")
    public ResponseEntity<?> analisisFinancieroHistoricoRangoFecha(
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime hasta,
            @RequestBody SolicitudAnalisisFinancieroHistoricoDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinancieroHistorico(dto, desde,hasta);
        return ResponseEntity.ok(dtoRespuesta);
    }

    // TODO: Probar modelo python
    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorialAnalisisFinanciero() {
        List<RespuestaAnalisisFinancieroDTO> historial = service.obtenerHistorialAnalisisFinanciero();
        return ResponseEntity.ok(historial);
    }
}
