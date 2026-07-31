package com.financeai.services;

import com.financeai.repository.UserRepository;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financeai.config.exceptions.ExcepcionEntidadNoEncontrada;
import com.financeai.dtos.RespuestaAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudAnalisisFinancieroDTO;
import com.financeai.dtos.SolicitudAnalisisFinancieroHistoricoDTO;
import com.financeai.dtos.SolicitudClasificarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.integrations.FinanceAiModelAdapter;
import com.financeai.models.AnalisisFinanciero;
import com.financeai.models.Transaccion;
import com.financeai.models.Usuario;
import com.financeai.repository.TransaccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalisisFinancieroService {
    private final UserRepository userRepository;
    final private FinanceAiModelAdapter modelAdapter;
    final private ClasificarTransaccionService clasificarTransaccionService;
    private final TransaccionRepository transaccionRepository;

    public RespuestaAnalisisFinancieroDTO realizarAnalisisFinanciero(SolicitudAnalisisFinancieroDTO dto) {
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

    @Transactional
    public RespuestaAnalisisFinancieroDTO realizarAnalisisFinancieroHistorico(SolicitudAnalisisFinancieroHistoricoDTO dto) {
    
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) throw new ExcepcionEntidadNoEncontrada("Usuario");

        userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        String username = usuario.getUsername();

        List<Transaccion> transacciones = new ArrayList<>();

        if (dto.getFechaFinPeriodo() == null && dto.getFechaFinPeriodo() == null) {
            
            transacciones = transaccionRepository.buscarTransacciones(username);

        } else if (dto.getFechaFinPeriodo() == null) {

            LocalDateTime fechaHoraInicio = dto.getFechaInicioPeriodo().atStartOfDay();
            transacciones = transaccionRepository.buscarTransaccionesDesde(fechaHoraInicio, username);

        } else if (dto.getFechaInicioPeriodo() == null) {

            LocalDateTime fechaHoraFin = dto.getFechaFinPeriodo().atStartOfDay().plusHours(24);
            transacciones = transaccionRepository.buscarTransaccionesHasta(fechaHoraFin, username);

        } else {

            LocalDateTime fechaHoraInicio = dto.getFechaInicioPeriodo().atStartOfDay();
            LocalDateTime fechaHoraFin = dto.getFechaFinPeriodo().atStartOfDay().plusHours(24);
            transacciones = transaccionRepository.buscarTransaccionesEntre(fechaHoraInicio, fechaHoraFin, username);

        }

        Map<String, Double> resumenGastos = new HashMap<>();
        Double gastoTotal = 0.0;

        for (Transaccion transaccion : transacciones) {
            gastoTotal += transaccion.getMonto();
            resumenGastos.merge(transaccion.getCategoria(), transaccion.getMonto(), Double::sum);
        }

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("ingreso_mensual", dto.getIngresoMensual());
        solicitud.put("frecuencia_ahorro", dto.getFrecuenciaAhorro());
        solicitud.put("nivel_endeudamiento", dto.getNivelEndeudamiento());
        solicitud.put("gasto_total", gastoTotal);

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

        AnalisisFinanciero analisisFinanciero = AnalisisFinanciero.builder()
                                                    .ingresoMensual(dto.getIngresoMensual())
                                                    .frecuenciaAhorro(dto.getFrecuenciaAhorro())
                                                    .nivelEndeudamiento(dto.getNivelEndeudamiento())
                                                    .perfilFinanciero(dtoRespuesta.getPerfilFinanciero())
                                                    .probabilidad(dtoRespuesta.getProbabilidad())
                                                    .recomendaciones(recomendaciones)
                                                    .resumenGastos(resumenGastos)
                                                    .transacciones(transacciones)
                                                    .build();

        usuario.getAnalisisFinancieros().add(analisisFinanciero);

        userRepository.save(usuario);

        return dtoRespuesta;
    } 

    // Se actualizo para soportar rango de fechas (sobrecarga de metodo por ahora)
    @Transactional
    public RespuestaAnalisisFinancieroDTO realizarAnalisisFinancieroHistorico(
            SolicitudAnalisisFinancieroHistoricoDTO dto,
            LocalDateTime desde,
            LocalDateTime hasta) {
    
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) throw new ExcepcionEntidadNoEncontrada("Usuario");

        userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        String username = usuario.getUsername();

        List<Transaccion> transacciones = new ArrayList<>();

        if (desde == null && hasta == null) {
            
            transacciones = transaccionRepository.buscarTransacciones(username);

        } else if (desde != null && hasta == null) {

            transacciones = transaccionRepository.buscarTransaccionesDesde(desde, username);

        } else if (desde == null) {

            LocalDateTime fechaHoraFin = hasta.with(LocalTime.MAX);
            transacciones = transaccionRepository.buscarTransaccionesHasta(fechaHoraFin, username);

        } else {

            LocalDateTime fechaHoraFin = hasta.with(LocalTime.MAX);
            transacciones = transaccionRepository.buscarTransaccionesEntre(desde, fechaHoraFin, username);

        }

        Map<String, Double> resumenGastos = new HashMap<>();
        Double gastoTotal = 0.0;

        for (Transaccion transaccion : transacciones) {
            gastoTotal += transaccion.getMonto();
            resumenGastos.merge(transaccion.getCategoria(), transaccion.getMonto(), Double::sum);
        }

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("ingreso_mensual", dto.getIngresoMensual());
        solicitud.put("frecuencia_ahorro", dto.getFrecuenciaAhorro());
        solicitud.put("nivel_endeudamiento", dto.getNivelEndeudamiento());
        solicitud.put("gasto_total", gastoTotal);

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

        AnalisisFinanciero analisisFinanciero = AnalisisFinanciero.builder()
                                                    .id(usuario.getId())
                                                    .fechaRealizacion(dto.getFechaInicioPeriodo() != null ? dto.getFechaInicioPeriodo().atStartOfDay() : LocalDateTime.now())
                                                    .ingresoMensual(dto.getIngresoMensual())
                                                    .frecuenciaAhorro(dto.getFrecuenciaAhorro())
                                                    .nivelEndeudamiento(dto.getNivelEndeudamiento())
                                                    .perfilFinanciero(dtoRespuesta.getPerfilFinanciero())
                                                    .probabilidad(dtoRespuesta.getProbabilidad())
                                                    .recomendaciones(recomendaciones)
                                                    .resumenGastos(resumenGastos)
                                                    .transacciones(transacciones)
                                                    .build();

        usuario.getAnalisisFinancieros().add(analisisFinanciero);

        userRepository.save(usuario);

        return dtoRespuesta;
    } 

    
    private void validarSolicitud(SolicitudAnalisisFinancieroDTO dto) {
        if (dto.getTransacciones() == null || dto.getTransacciones().isEmpty()) {
            throw new IllegalArgumentException("La lista de transacciones no puede estar vacía");
        }
    }


}
