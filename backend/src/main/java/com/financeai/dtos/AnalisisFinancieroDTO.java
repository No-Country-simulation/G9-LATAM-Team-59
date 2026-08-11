package com.financeai.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnalisisFinancieroDTO {
    @JsonProperty("fecha_realizacion")
    private LocalDateTime fechaRealizacion;
    
    @JsonProperty("ingreso_mensual")
    private Double ingresoMensual;

    @JsonProperty("nivel_endeudamiento")
    private Double nivelEndeudamiento;

    @JsonProperty("frecuencia_ahorro")
    private String frecuenciaAhorro;

    private List<TransaccionDTO> transacciones = new ArrayList<>();

    @JsonProperty("perfil_financiero")
    private String perfilFinanciero;

    private Map<String, Double> probabilidad = new HashMap<>();

    @JsonProperty("resumen_gastos")
    private Map<String, Double> resumenGastos = new HashMap<>();

    private String recomendaciones;

    @JsonProperty("moneda_ingreso_mensual")
    private String monedaIngresoMensual;
}
