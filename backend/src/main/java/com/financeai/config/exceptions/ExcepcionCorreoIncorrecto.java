package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionCorreoIncorrecto extends ExcepcionNegocio {

    public ExcepcionCorreoIncorrecto() {
        super("Correo ingresado incorrecto");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
