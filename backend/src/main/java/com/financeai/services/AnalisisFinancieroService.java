package com.financeai.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financeai.dtos.RespuestaAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudAnalisisFinancieroDTO;
import com.financeai.dtos.TransaccionClasificadaDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.integrations.FinanceAiModelAdapter;
import com.financeai.models.Transaccion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalisisFinancieroService {
    final private FinanceAiModelAdapter modelAdapter;
    final private ClasificarTransaccionService clasificarTransaccionService;

    public RespuestaAnalisisFinancieroDTO clasificarTransacciones(SolicitudAnalisisFinancieroDTO dto) {
        validarSolicitud(dto);

        Double gastoTotal = 0.0;

        List<TransaccionDTO> transacciones = dto.getTransacciones();
        Map<String, Double> resumenGastos = new HashMap<>();

        for (TransaccionDTO transaccion : transacciones) {
            gastoTotal += transaccion.getMonto();
            TransaccionClasificadaDTO dtoRespuesta = modelAdapter.conectarModeloFinanceAI(
                "/clasificar", 
                descripcion, 
                TransaccionClasificadaDTO.class); 


        }


        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("ingreso_mensual", dto.getIngresoMensual());
        solicitud.put("frecuencia_ahorro", dto.getFrecuenciaAhorro());
        solicitud.put("nivel_endeudamiento", dto.getNivelEndeudamiento());
        solicitud.put("gasto_total", dto.getTransacciones().stream()
                                    .map(t -> t.getMonto())
                                    .collect(Collectors.summingDouble(null))
        );

        RespuestaAnalisisFinancieroDTO dtoRespuesta = modelAdapter.conectarModeloFinanceAI(
                "/analisis", 
                solicitud, 
                RespuestaAnalisisFinancieroDTO.class);   

        dtoRespuesta.

        return dtoRespuesta;
    }

    private void validarSolicitud(SolicitudAnalisisFinancieroDTO dto) {
        if (dto.getTransacciones() == null || dto.getTransacciones().isEmpty()) {
            throw new IllegalArgumentException("La lista de transacciones no puede estar vacía");
        }
    }


}
