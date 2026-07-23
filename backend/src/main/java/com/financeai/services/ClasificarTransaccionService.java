package com.financeai.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.financeai.dtos.RespuestaClasificarTransaccionesDTO;
import com.financeai.dtos.SolicitudClasificarTransaccionesDTO;
import com.financeai.integrations.FinanceAiModelAdapter;

@Service
public class ClasificarTransaccionService {

    private final FinanceAiModelAdapter modelAdapter;

    public ClasificarTransaccionService(FinanceAiModelAdapter modelAdapter) {
        this.modelAdapter = modelAdapter;
    }
    /*
    TODO: Necesito especificar si necesito clasificar una transaccion o un conjunto de transacciones.
    */
    public RespuestaClasificarTransaccionesDTO clasificarTransacciones(SolicitudClasificarTransaccionesDTO dto) {
        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("transacciones", dto.getTransacciones());

        Map<String, Object> modelResponse = modelAdapter.conectarModeloFinanceAI("/clasificacion", solicitud, Map.class);

        RespuestaClasificarTransaccionesDTO respuesta = new RespuestaClasificarTransaccionesDTO();
        respuesta.setClasificaciones((Map<String, String>) modelResponse.get("clasificaciones"));
        
        return respuesta;
    }
}
