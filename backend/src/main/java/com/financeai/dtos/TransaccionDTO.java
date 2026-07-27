package com.financeai.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransaccionDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
    private String descripcion;

    @NotNull(message = "El valor no puede ser nulo")
    @Positive(message = "El valor debe ser mayor a cero")
    private Double valor;

}
