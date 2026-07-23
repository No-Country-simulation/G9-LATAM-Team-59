package com.financeai.dtos;

import java.util.Map;

public class RespuestaClasificarTransaccionesDTO {

    private Map<String, String> clasificaciones;

    public Map<String, String> getClasificaciones() { return clasificaciones; }
    public void setClasificaciones(Map<String, String> clasificaciones) { this.clasificaciones = clasificaciones; }

}
