package com.financeai.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.financeai.config.exceptions.ExcepcionEmailDuplicado;
import com.financeai.config.exceptions.ExcepcionNombreUsuarioDuplicado;
import com.financeai.dtos.LoginResponseDTO;
import com.financeai.dtos.UsuarioDTO;
import com.financeai.models.Usuario;
import com.financeai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarCuentaService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO registrarCuenta(UsuarioDTO usuario) {
        
        if (userRepository.existsByEmail(usuario.email())) {
            throw new ExcepcionEmailDuplicado(usuario.email());
        }

        if (userRepository.existsByUsername(usuario.username())) {
            throw new ExcepcionNombreUsuarioDuplicado(usuario.username());
        }
        
        Usuario newUser = new Usuario();
        newUser.setUsername(usuario.username());
        newUser.setEmail(usuario.email());
        newUser.setPassword(passwordEncoder.encode(usuario.password()));
        userRepository.save(newUser);
        
        return new LoginResponseDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), "Cuenta registrada exitosamente");
    }
}
