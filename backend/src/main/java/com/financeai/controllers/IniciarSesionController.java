package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.dtos.LoginRequestDTO;
import com.financeai.dtos.TokenResponseDTO;
import com.financeai.services.IniciarSesionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class IniciarSesionController {

    private final IniciarSesionService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        
        TokenResponseDTO tokenResponseDto = service.iniciarSesion(loginRequest);
        return ResponseEntity.ok(tokenResponseDto);

    }
}
