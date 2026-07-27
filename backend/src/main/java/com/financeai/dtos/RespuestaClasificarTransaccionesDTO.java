package com.financeai.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RespuestaClasificarTransaccionesDTO {

    private Map<String, Double> clasificaciones;

}
