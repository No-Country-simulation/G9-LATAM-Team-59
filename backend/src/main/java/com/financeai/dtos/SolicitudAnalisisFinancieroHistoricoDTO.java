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

    @NotNull(message = "Ingreso mensual no puede ser nulo (HISTORICO)")
    @PositiveOrZero(message = "Ingreso mensual debe ser mayor o igual a 0 (HISTORICO)")
    private Double ingresoMensual;

    @NotNull(message = "Nivel de endeudamiento no puede ser un valor nulo, debe ser un valor entre 0 y 100 (HISTORICO)")
    @Min(0)
    @Max(100)
    private Double nivelEndeudamiento;

    @NotBlank(message = "No puedes dejar vacío el campo Frecuencia de Ahorro debes especificar nivel (Nula, Baja, Media o Alta) (HISTORICO)")
    @Pattern(regexp = "Nula|Baja|Media|Alta")
    private String frecuenciaAhorro;

    private LocalDate fechaInicioPeriodo;

    private LocalDate fechaFinPeriodo;
}
