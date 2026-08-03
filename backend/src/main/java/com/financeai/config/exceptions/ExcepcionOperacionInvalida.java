package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionOperacionInvalida extends ExcepcionNegocio {

    public ExcepcionOperacionInvalida(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
