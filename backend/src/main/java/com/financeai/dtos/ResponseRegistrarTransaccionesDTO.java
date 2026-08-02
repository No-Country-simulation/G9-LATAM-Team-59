package com.financeai.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class ResponseRegistrarTransaccionesDTO {
    private Long id;
    private String descripcion;
    private Double monto;
    private String categoria;
    private LocalDateTime fechaSubida;
}
