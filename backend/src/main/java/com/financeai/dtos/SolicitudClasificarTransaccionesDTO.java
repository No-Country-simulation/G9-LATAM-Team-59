package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class SolicitudClasificarTransaccionesDTO {

    @NotEmpty
    @Valid
    private List<TransaccionDTO> transacciones;

    public List<TransaccionDTO> getTransacciones() { return transacciones; }
    public void setTransacciones(List<TransaccionDTO> transacciones) { this.transacciones = transacciones; }

}
