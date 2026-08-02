package com.financeai.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyColumn;
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
    private LocalDateTime fechaRealizacion;
    
    @Column(nullable = false)
    private Double ingresoMensual;

    @Column(nullable = false)
    private Double nivelEndeudamiento;

    @Column(nullable = false)
    private String frecuenciaAhorro;

    @ManyToMany
    @JoinTable(
        name = "analisis_transacciones",
        joinColumns = @JoinColumn(name = "analisis_id"),
        inverseJoinColumns = @JoinColumn(name = "transaccion_id")
    )
    @Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();
    
    @Column(nullable = false)
    private String perfilFinanciero;

    @ElementCollection
    @CollectionTable(name = "analisis_probabilidad", joinColumns = @JoinColumn(name = "analisis_id"))
    @MapKeyColumn(name = "clave")
    @Column(name = "valor")
    @Builder.Default
    private Map<String, Double> probabilidad = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "analisis_resumen_gastos", joinColumns = @JoinColumn(name = "analisis_id"))
    @MapKeyColumn(name = "categoria")
    @Column(name = "monto")
    @Builder.Default
    private Map<String, Double> resumenGastos = new HashMap<>();

    @Column(nullable = false)
    private String recomendaciones;

}
