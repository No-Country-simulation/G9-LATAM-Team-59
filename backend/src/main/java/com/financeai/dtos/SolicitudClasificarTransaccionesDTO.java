package com.financeai.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SolicitudClasificarTransaccionesDTO {

    @NotEmpty(message = "Se necesita una o más transacciones")
    @Valid
    private List<TransaccionDTO> transacciones;

}
