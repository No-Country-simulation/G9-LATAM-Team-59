package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SolicitudAnalisisFinancieroDTO {

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

    @NotEmpty
    @Valid
    private List<TransaccionDTO> transacciones;
}
