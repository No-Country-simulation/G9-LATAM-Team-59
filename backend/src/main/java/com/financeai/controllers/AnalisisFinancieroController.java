package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> realizarAnalisisFinanciero(@RequestBody @Valid SolicitudAnalisisFinancieroDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinanciero(dto);
        return ResponseEntity.ok(dtoRespuesta);
    }

     @PostMapping("/mis-transacciones")
     public ResponseEntity<?> realizarAnalisisFinancieroHistorico(
             @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
             @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
             @RequestBody @Valid SolicitudAnalisisFinancieroHistoricoDTO dto) {
         RespuestaAnalisisFinancieroDTO dtoRespuesta = service.realizarAnalisisFinancieroHistorico(desde, hasta, dto);
         return ResponseEntity.ok(dtoRespuesta);
     }


    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorialAnalisisFinanciero() {
        List<RespuestaAnalisisFinancieroDTO> historial = service.obtenerHistorialAnalisisFinanciero();
        return ResponseEntity.ok(historial);
    }
}
