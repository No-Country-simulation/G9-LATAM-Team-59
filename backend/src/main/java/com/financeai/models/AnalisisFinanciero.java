package com.financeai.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalisisFinanciero {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double ingresoMensual;

    @Column(nullable = false)
    private Double nivelEndeudamiento;

    @Column(nullable = false)
    private String frecuenciaAhorro;

    @Column(nullable = true)
    @Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();;
    
    @Column(nullable = false)
    private String perfilFinanciero;

    @Column(nullable = false)
    private Map<String, Double> probabilidad;

    @Column(nullable = false)
    private Map<String, Double> resumenGastos;

    @Column(nullable = false)
    private String recomendaciones;

}
