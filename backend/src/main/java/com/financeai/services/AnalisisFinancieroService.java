package com.financeai.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.financeai.dtos.RespuestaAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudAnalisisFinancieroDTO;
import com.financeai.integrations.FinanceAiModelAdapter;

@Service
public class AnalisisFinancieroService {
    final private FinanceAiModelAdapter modelAdapter;

    public AnalisisFinancieroService(FinanceAiModelAdapter modelAdapter) {
        this.modelAdapter = modelAdapter;
    }

    public RespuestaAnalisisFinancieroDTO clasificarTransacciones(SolicitudAnalisisFinancieroDTO dto) {
        validarSolicitud(dto);

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("ingreso_mensual", dto.getIngreso_mensual());
        solicitud.put("frecuencia_ahorro", dto.getFrecuencia_ahorro());
        solicitud.put("nivel_endeudamiento", dto.getNivel_endeudamiento());
        solicitud.put("transacciones", dto.getTransacciones());

        Map<String, Object> modelResponse = modelAdapter.conectarModeloFinanceAI("/analisis", solicitud, Map.class);

        RespuestaAnalisisFinancieroDTO dtoRespuesta = new RespuestaAnalisisFinancieroDTO();
        dtoRespuesta.setAnalisis((String) modelResponse.get("analisis"));
        dtoRespuesta.setRecomendaciones((String) modelResponse.get("recomendaciones"));

        return dtoRespuesta;
    }

    private void validarSolicitud(SolicitudAnalisisFinancieroDTO dto) {
        if (dto.getTransacciones() == null || dto.getTransacciones().isEmpty()) {
            throw new IllegalArgumentException("La lista de transacciones no puede estar vacía");
        }
    }


}
