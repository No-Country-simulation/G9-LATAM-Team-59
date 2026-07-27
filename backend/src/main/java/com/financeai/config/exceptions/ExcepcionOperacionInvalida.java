package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionOperacionInvalida extends ExcepcionNegocio {

    public ExcepcionOperacionInvalida(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
