package com.financeai.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.financeai.dtos.RespuestaClasificarTransaccionesDTO;
import com.financeai.dtos.SolicitudClasificarTransaccionesDTO;
import com.financeai.dtos.TransaccionClasificadaDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.integrations.CurrencyAdapter;
import com.financeai.integrations.FinanceAiModelAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClasificarTransaccionService {

    private final FinanceAiModelAdapter modelAdapter;
    private final CurrencyAdapter currencyAdapter;

    public RespuestaClasificarTransaccionesDTO clasificarTransacciones(SolicitudClasificarTransaccionesDTO dto) {
        
        List<TransaccionDTO> transacciones = dto.getTransacciones();
        Map<String, Double> resumenGastos = new HashMap<>();

        for (TransaccionDTO transaccion : transacciones) {
            
            String moneda = transaccion.getMoneda();

            Double rate = currencyAdapter.getConversionRate(moneda, "USD").rate();

            String categoria = clasificarTransaccion(transaccion);
            
            resumenGastos.merge(categoria, transaccion.getMonto()*rate, Double::sum);
        }

        RespuestaClasificarTransaccionesDTO respuesta = new RespuestaClasificarTransaccionesDTO(resumenGastos);
        
        return respuesta;
    }

    public String clasificarTransaccion(TransaccionDTO transaccionDTO) {

        TransaccionClasificadaDTO modelResponse = modelAdapter.conectarModeloFinanceAI(
                "/api/clasificacion", 
                transaccionDTO, 
                TransaccionClasificadaDTO.class);

        return modelResponse.categoria();
    }

}
