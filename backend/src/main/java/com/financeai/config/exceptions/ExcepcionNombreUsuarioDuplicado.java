package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionNombreUsuarioDuplicado extends ExcepcionNegocio {

    public ExcepcionNombreUsuarioDuplicado(String nombreUsuario) {
        super("El nombre de usuario " + nombreUsuario + " ya se encuentra registrado.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
