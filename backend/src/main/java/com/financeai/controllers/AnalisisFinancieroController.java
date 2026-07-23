package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.services.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analisis-financiero")
public class AnalisisFinancieroController {
    private final AnalisisFinancieroService service;

    @PostMapping
    public ResponseEntity<?> clasificarTransacciones(@RequestBody SolicitudAnalisisFinancieroDTO dto) {
        RespuestaAnalisisFinancieroDTO dtoRespuesta = service.clasificarTransacciones(dto);
        
        return ResponseEntity.ok(dtoRespuesta);
    }
}
