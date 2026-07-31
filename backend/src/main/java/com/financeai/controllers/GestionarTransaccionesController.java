package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
