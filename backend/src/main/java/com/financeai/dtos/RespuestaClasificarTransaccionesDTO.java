package com.financeai.dtos;

import java.util.Map;

public class RespuestaClasificarTransaccionesDTO {

    private Map<String, String> clasificaciones;
    private Map<String, Object> informacionAdicional;

    public Map<String, String> getClasificaciones() { return clasificaciones; }
    public void setClasificaciones(Map<String, String> clasificaciones) { this.clasificaciones = clasificaciones; }

    public Map<String, Object> getInformacionAdicional() { return informacionAdicional; }
    public void setInformacionAdicional(Map<String, Object> informacionAdicional) { this.informacionAdicional = informacionAdicional; }

}
