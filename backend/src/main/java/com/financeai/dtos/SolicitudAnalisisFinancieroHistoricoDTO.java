package com.financeai.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SolicitudAnalisisFinancieroHistoricoDTO {

    @NotNull
    @PositiveOrZero
    private Double ingresoMensual;

    @NotNull
    @Min(0)
    @Max(100)
    private Double nivelEndeudamiento;

    @NotBlank
    @Pattern(regexp = "Nula|Baja|Media|Alta")
    private String frecuenciaAhorro;

    private LocalDate fechaInicioPeriodo;

    private LocalDate fechaFinPeriodo;
}
