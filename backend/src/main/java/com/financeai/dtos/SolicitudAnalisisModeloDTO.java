package com.financeai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SolicitudAnalisisModeloDTO {
    @JsonProperty("frecuencia_ahorro")
    private String frecuencia_ahorro;

    @JsonProperty("ingreso_mensual")
    private Double ingreso_mensual;

    @JsonProperty("nivel_endeudamiento")
    private Double nivel_endeudamiento;

    @JsonProperty("gasto_total")
    private Double gasto_total;

}
