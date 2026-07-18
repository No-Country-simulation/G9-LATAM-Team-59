package com.financeai.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

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
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("transacciones", dto.getTransacciones());
        requestData.put("informacion_adicional", dto.getInformacionAdicional());

        Map<String, Object> modelResponse = modelAdapter.clasificarTransacciones(requestData);

        RespuestaClasificarTransaccionesDTO respuesta = new RespuestaClasificarTransaccionesDTO();
        respuesta.setClasificaciones((Map<String, String>) modelResponse.get("clasificaciones"));
        respuesta.setInformacionAdicional((Map<String, Object>) modelResponse.get("informacion_adicional"));

        return respuesta;
    }
}
