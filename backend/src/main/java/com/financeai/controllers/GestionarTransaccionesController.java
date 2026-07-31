package com.financeai.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.dtos.ResponseRegistrarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.models.Transaccion;
import com.financeai.services.GestionarTransaccionesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GestionarTransaccionesController {
    private final GestionarTransaccionesService gestionarTransaccionesService;

    @PostMapping("/transacciones")
    public ResponseEntity<?> registrarTransaccion(@RequestBody @Valid TransaccionDTO transaccionDTO) {
        ResponseRegistrarTransaccionesDTO dto = gestionarTransaccionesService.registrarTransaccion(transaccionDTO);
        return ResponseEntity.ok(dto);
        
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<Transaccion>> verTransaccionesRangoFecha(
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime hasta) {
        List<Transaccion> transacciones = gestionarTransaccionesService.verTransaccionesRangoFecha(desde, hasta);
        return ResponseEntity.ok(transacciones);
    }

    @DeleteMapping("/transacciones/{id}")
    public ResponseEntity<?> eliminarTransaccion(@PathVariable(name = "id") Long id) {
        gestionarTransaccionesService.eliminarTransaccion(id);
        return ResponseEntity.noContent().build();
    }
}
