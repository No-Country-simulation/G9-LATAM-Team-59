package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionEmailDuplicado extends ExcepcionNegocio {

    public ExcepcionEmailDuplicado(String email) {
        super("El email " + email + " ya se encuentra registrado.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
