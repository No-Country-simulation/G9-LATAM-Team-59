package com.financeai.controllers;

import com.financeai.dtos.*;
import com.financeai.models.Transaccion;
import com.financeai.services.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/historico")
    public ResponseEntity<List<Transaccion>> clasificarTransaccionesRegistradas(
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime hasta) {

                
        
    }
}
