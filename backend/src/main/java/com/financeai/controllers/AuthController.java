package com.financeai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.dtos.LoginRequestDto;
import com.financeai.dtos.TokenResponseDto;
import com.financeai.models.Usuario;
import com.financeai.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    
    @Autowired
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    protected TokenService tokenService;

}
