package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ExcepcionNegocio extends RuntimeException{

    protected ExcepcionNegocio(String mensaje){
        super(mensaje);
    }

    public abstract HttpStatus getStatus();

}
