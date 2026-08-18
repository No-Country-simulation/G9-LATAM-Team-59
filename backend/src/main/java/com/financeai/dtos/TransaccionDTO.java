package com.financeai.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransaccionDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
    private String descripcion;

    @NotNull(message = "El valor no puede ser nulo")
    @Positive(message = "El valor debe ser mayor a cero")
    private Double monto;

    private Long id;

    private String moneda = "USD";

    @JsonProperty("fecha_subida")
    private LocalDateTime fechaSubida;

    public TransaccionDTO(String descripcion, Double monto, Long id, String moneda) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.id = id;
        this.moneda = moneda;
    }
}

