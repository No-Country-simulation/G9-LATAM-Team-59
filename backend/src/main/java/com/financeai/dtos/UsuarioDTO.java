package com.financeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO (
    @NotBlank(message = "Campo email no puede estar vacío") @Email String email,
    @NotBlank(message = "Contraseña es requerida") String password,
    @NotBlank(message = "Se necesita un nombre de usuario") String username
) 
{}
