package com.financeai.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.dtos.ResponseRegistrarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.services.GestionarTransaccionesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GestionarTransaccionesController {
    private final GestionarTransaccionesService gestionarTransaccionesService;

    @GetMapping("/transacciones")
    public ResponseEntity<?> verTransacciones(
        @RequestParam("desde") LocalDateTime fechaInicio,
        @RequestParam("hasta") LocalDateTime fechaHasta
    ) {
        List<TransaccionDTO> dto = gestionarTransaccionesService.verTransaccionesRangoFecha(fechaInicio, fechaHasta);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/transacciones")
    public ResponseEntity<?> registrarTransaccion(@RequestBody @Valid TransaccionDTO transaccionDTO) {
        ResponseRegistrarTransaccionesDTO dto = gestionarTransaccionesService.registrarTransaccion(transaccionDTO);
        return ResponseEntity.ok(dto);
        
    }

    @DeleteMapping("/transacciones/{id}")
    public ResponseEntity<?> eliminarTransaccion(@PathVariable(name = "id") Long id) {
        gestionarTransaccionesService.eliminarTransaccion(id);
        return ResponseEntity.noContent().build();
    }
}
