package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class SolicitudAnalisisFinancieroDTO {

    @NotNull
    @PositiveOrZero
    private Double ingreso_mensual;

    @NotNull
    @Min(0)
    @Max(100)
    private Double nivel_endeudamiento;

    @NotBlank
    @Pattern(regexp = "Nula|Baja|Media|Alta")
    private String frecuencia_ahorro;

    @NotEmpty
    @Valid
    private List<TransaccionDTO> transacciones;

    private Map<String, Object> informacionAdicional;

    public Double getIngreso_mensual() { return ingreso_mensual; }
    public void setIngreso_mensual(Double ingreso_mensual) { this.ingreso_mensual = ingreso_mensual; }

    public Double getNivel_endeudamiento() { return nivel_endeudamiento; }
    public void setNivel_endeudamiento(Double nivel_endeudamiento) { this.nivel_endeudamiento = nivel_endeudamiento; }

    public String getFrecuencia_ahorro() { return frecuencia_ahorro; }
    public void setFrecuencia_ahorro(String frecuencia_ahorro) { this.frecuencia_ahorro = frecuencia_ahorro; }

    public List<TransaccionDTO> getTransacciones() { return transacciones; }
    public void setTransacciones(List<TransaccionDTO> transacciones) { this.transacciones = transacciones; }

    public Map<String, Object> getInformacionAdicional() { return informacionAdicional; }
    public void setInformacionAdicional(Map<String, Object> informacionAdicional) { this.informacionAdicional = informacionAdicional; }

}
