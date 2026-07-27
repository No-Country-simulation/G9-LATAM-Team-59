package com.financeai.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Transaccion {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHoraRegistro;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String monto;

    @Column(nullable = false)
    private String categoria;
}
