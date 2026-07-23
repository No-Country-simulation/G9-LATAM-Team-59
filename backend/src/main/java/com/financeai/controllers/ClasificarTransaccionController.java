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
@RequestMapping("/api/clasificar-transacciones")
public class ClasificarTransaccionController {
    private final ClasificarTransaccionService service;

    @PostMapping
    public ResponseEntity<?> clasificarTransacciones(@RequestBody SolicitudClasificarTransaccionesDTO dto) {
        RespuestaClasificarTransaccionesDTO dtoRespuesta = service.clasificarTransacciones(dto);
        
        return ResponseEntity.ok(dtoRespuesta);
    }
}
