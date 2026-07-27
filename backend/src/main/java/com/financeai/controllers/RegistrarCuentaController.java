package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.dtos.UsuarioDTO;
import com.financeai.services.RegistrarCuentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class RegistrarCuentaController {
    private final RegistrarCuentaService service;

    @PostMapping("/registrar-cuenta")
    public ResponseEntity<?> login(@RequestBody @Valid UsuarioDTO dto) {
        
        service.registrarCuenta(dto);
        return ResponseEntity.ok().build();

    }
}   
