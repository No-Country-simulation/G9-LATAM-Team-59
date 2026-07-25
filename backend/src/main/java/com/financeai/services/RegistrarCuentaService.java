package com.financeai.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.financeai.dtos.UsuarioDto;
import com.financeai.models.Usuario;
import com.financeai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarCuentaService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registrarCuenta(UsuarioDto usuario) {
        
        if (userRepository.existsByEmail(usuario.email())) {
            throw new RuntimeException();
        }

        if (userRepository.existsByUsername(usuario.userName())) {
            throw new RuntimeException();
        }
        
        Usuario newUser = new Usuario();
        newUser.setUsername(usuario.userName());
        newUser.setEmail(usuario.email());
        newUser.setPassword(passwordEncoder.encode(usuario.password()));
        
        userRepository.save(newUser);
        
        return;
    }
}
