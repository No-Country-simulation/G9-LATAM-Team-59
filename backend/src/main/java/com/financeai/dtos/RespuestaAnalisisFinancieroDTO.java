package com.financeai.dtos;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RespuestaAnalisisFinancieroDTO {

    private String perfilFinanciero;
    private Map<String, Double> probabilidad;
    private Map<String, Double> resumenGastos;
    private String recomendaciones;

}
