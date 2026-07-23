package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.models.Usuario;
import com.financeai.repository.UserRepository;

@RestController
@RequestMapping("api/auth")
public class RegistrarCuentaController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrarCuentaController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        if (userRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El email ya está registrado.");
        }

        if (userRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está registrado.");
        }
        
        Usuario newUser = new Usuario();
        newUser.setUsername(usuario.getUsername());
        newUser.setEmail(usuario.getEmail());
        newUser.setPassword(passwordEncoder.encode(usuario.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }
}
