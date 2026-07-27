package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionRecursoNoEncontrado extends ExcepcionNegocio {
    public ExcepcionRecursoNoEncontrado(String nombreRecurso, String id) {
        super(nombreRecurso + " con id " + id + " no encontrado");
    }

    public HttpStatus getStatus() {
        return  HttpStatus.NOT_FOUND;
    }
}