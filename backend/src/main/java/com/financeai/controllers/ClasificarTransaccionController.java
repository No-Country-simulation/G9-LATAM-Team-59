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

    // Ahora se comunicará con el modelo de IA
    @PostMapping
    public ResponseEntity<?> clasificarTransacciones(@RequestBody SolicitudClasificarTransaccionesDTO dto) {
        RespuestaClasificarTransaccionesDTO dtoRespuesta = service.clasificarTransacciones(dto);
        
        // RespuestaClasificarTransaccionesDTO dtoRespuesta = new RespuestaClasificarTransaccionesDTO();
        // dtoRespuesta.setClasificaciones(Map.of(
        //     "Alimentación", 1000.2,
        //     "Entretenimiento", 900.0,
        //     "Transporte", 2000.0
        // ));
        
        return ResponseEntity.ok(dtoRespuesta);
    }
}
