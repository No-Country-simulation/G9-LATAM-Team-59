package com.financeai.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RespuestaAnalisisFinancieroDTO {

    @JsonProperty("perfil_financiero")
    private String perfilFinanciero;
    
    @JsonProperty("probabilidad")
    private Map<String, Double> probabilidad;

    @JsonProperty("resumen_gastos")
    private Map<String, Double> resumenGastos;

    @JsonProperty("recomendaciones")
    private String recomendaciones;

}
