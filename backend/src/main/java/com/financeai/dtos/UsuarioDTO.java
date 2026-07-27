package com.financeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO (
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String username
) 
{}
