package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
