package com.financeai.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.financeai.dtos.LoginRequestDto;
import com.financeai.dtos.TokenResponseDto;
import com.financeai.models.Usuario;
import com.financeai.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IniciarSesionService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${api.security.token.secret}")
    private String secret;

    public TokenResponseDto iniciarSesion (LoginRequestDto dto) {

        Usuario usuario = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException());

        if(!passwordEncoder.matches(dto.password(), usuario.getPassword())) 
            throw new RuntimeException();
        
        String token = Jwts.builder()
            .subject(usuario.getEmail())
            .issuer("Forum API")
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(86400))) // 24 hours
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
        
        return new TokenResponseDto(token);

    }
}
