package com.financeai.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SolicitudAnalisisFinancieroHistoricoDTO {

    @JsonProperty("ingreso_mensual")
    @NotNull(message = "Ingreso mensual no puede ser nulo (HISTORICO)")
    @PositiveOrZero(message = "Ingreso mensual debe ser mayor o igual a 0 (HISTORICO)")
    private Double ingresoMensual;

    @NotNull(message = "Nivel de endeudamiento no puede ser un valor nulo, debe ser un valor entre 0 y 100 (HISTORICO)")
    @Min(0)
    @Max(100)
    @JsonProperty("nivel_endeudamiento")
    private Double nivelEndeudamiento;

    @JsonProperty("frecuencia_ahorro")
    @NotBlank(message = "No puedes dejar vacío el campo Frecuencia de Ahorro debes especificar nivel (Nula, Baja, Media o Alta) (HISTORICO)")
    @Pattern(regexp = "Nula|Baja|Media|Alta")
    private String frecuenciaAhorro;

    @JsonProperty("fecha_inicio_periodo")
    private LocalDate fechaInicioPeriodo;

    @JsonProperty("fecha_fin_periodo")
    private LocalDate fechaFinPeriodo;
}
