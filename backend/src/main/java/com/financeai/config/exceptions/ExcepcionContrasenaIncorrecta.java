package com.financeai.config.exceptions;

import org.springframework.http.HttpStatus;

public class ExcepcionContrasenaIncorrecta extends ExcepcionNegocio {
    
    public ExcepcionContrasenaIncorrecta() {
        super("La contraseña es incorrecta");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
