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

    @NotEmpty
    @Valid
    private List<TransaccionDTO> transacciones;

}
