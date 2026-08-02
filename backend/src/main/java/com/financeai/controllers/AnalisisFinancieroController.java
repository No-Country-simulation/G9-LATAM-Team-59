package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analisis-financiero")
public class AnalisisFinancieroController {
    private final AnalisisFinancieroService service;

    @PostMapping
    public ResponseEntity<?> realizarAnalisisFinanciero(@RequestBody SolicitudAnalisisFinancieroDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinanciero(dto);
        return ResponseEntity.ok(dtoRespuesta);
    }

     @PostMapping("/mis-transacciones")
     public ResponseEntity<?> realizarAnalisisFinancieroHistorico(@RequestBody SolicitudAnalisisFinancieroHistoricoDTO dto) {
         RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinancieroHistorico(dto);
         return ResponseEntity.ok(dtoRespuesta);
     }


    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorialAnalisisFinanciero() {
        List<RespuestaAnalisisFinancieroDTO> historial = service.obtenerHistorialAnalisisFinanciero();
        return ResponseEntity.ok(historial);
    }
}
