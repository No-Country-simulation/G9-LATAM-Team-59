package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SolicitudAnalisisFinancieroDTO {

    @JsonProperty("ingreso_mensual")
    @NotNull(message = "Ingreso mensual no puede ser nulo")
    @PositiveOrZero(message = "Ingreso mensual debe ser mayor o igual a 0")
    private Double ingresoMensual;

    @JsonProperty("nivel_endeudamiento")
    @NotNull(message = "Nivel de endeudamiento no puede ser un valor nulo, debe ser un valor entre 0 y 100")
    @Min(0)
    @Max(100)
    private Double nivelEndeudamiento;

    @JsonProperty("frecuencia_ahorro")
    @NotBlank(message = "No puedes dejar vacío el campo Frecuencia de Ahorro")
    @Pattern(regexp = "Nula|Baja|Media|Alta")
    private String frecuenciaAhorro;

    @NotEmpty(message = "Se necesita una o más transacciones")
    @Valid
    private List<TransaccionDTO> transacciones;

    @JsonProperty("moneda_ingreso_mensual")
    private String monedaIngresoMensual = "USD";
}
