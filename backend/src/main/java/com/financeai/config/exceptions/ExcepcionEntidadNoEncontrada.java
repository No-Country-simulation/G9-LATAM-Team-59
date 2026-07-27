package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionEntidadNoEncontrada extends ExcepcionNegocio {

    public ExcepcionEntidadNoEncontrada(String entidad) {
        super("No se encontró la entidad: " + entidad);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
