package com.financeai.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.financeai.dtos.RespuestaAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudClasificarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.integrations.FinanceAiModelAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalisisFinancieroService {
    final private FinanceAiModelAdapter modelAdapter;
    final private ClasificarTransaccionService clasificarTransaccionService;

    public RespuestaAnalisisFinancieroDTO clasificarTransacciones(SolicitudAnalisisFinancieroDTO dto) {
        validarSolicitud(dto);

        List<TransaccionDTO> transacciones = dto.getTransacciones();
        Map<String, Double> resumenGastos = new HashMap<>();

        resumenGastos = clasificarTransaccionService.clasificarTransacciones(
                new SolicitudClasificarTransaccionesDTO(transacciones)).getClasificaciones();

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("ingreso_mensual", dto.getIngresoMensual());
        solicitud.put("frecuencia_ahorro", dto.getFrecuenciaAhorro());
        solicitud.put("nivel_endeudamiento", dto.getNivelEndeudamiento());
        solicitud.put("gasto_total", dto.getTransacciones().stream()
                                    .mapToDouble(TransaccionDTO::getMonto)
                                    .sum()
        );

        RespuestaAnalisisFinancieroDTO dtoRespuesta = modelAdapter.conectarModeloFinanceAI(
                "/analisis", 
                solicitud, 
                RespuestaAnalisisFinancieroDTO.class);   

        dtoRespuesta.setResumenGastos(resumenGastos);

        String recomendaciones = switch (dtoRespuesta.getPerfilFinanciero() != null ? dtoRespuesta.getPerfilFinanciero() : "") {
            case "Saludable" -> 
                "¡Excelente gestión financiera! Mantén tu hábito de ahorro activo y considera explorar opciones de inversión a mediano o largo plazo para hacer crecer tu patrimonio.";
            case "En observación" -> 
                "Tu situación financiera es estable, pero requiere precaución. Se sugiere revisar tus gastos no esenciales y fortalecer tu fondo de emergencia para evitar un posible sobreendeudamiento.";
            case "En riesgo" -> 
                "Atención: Tu nivel de gasto o endeudamiento es elevado en relación con tus ingresos. Te recomendamos priorizar el pago de deudas de alto interés, recortar gastos no prioritarios y elaborar un presupuesto mensual estricto.";
            default -> 
                "Te recomendamos revisar periódicamente tu presupuesto mensual y llevar un control constante de tus gastos.";
        };

        dtoRespuesta.setRecomendaciones(recomendaciones);

        return dtoRespuesta;
    }

    private void validarSolicitud(SolicitudAnalisisFinancieroDTO dto) {
        if (dto.getTransacciones() == null || dto.getTransacciones().isEmpty()) {
            throw new IllegalArgumentException("La lista de transacciones no puede estar vacía");
        }
    }


}
