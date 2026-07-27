package com.financeai.dtos;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaAnalisisFinancieroDTO {

    private String perfilFinanciero;
    private Double probabilidad;
    private Map<String, Double> resumenGastos;
    private String recomendaciones;

}
