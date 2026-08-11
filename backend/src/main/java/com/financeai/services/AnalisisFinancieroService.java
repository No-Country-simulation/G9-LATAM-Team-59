package com.financeai.services;

import com.financeai.repository.UserRepository;

import java.time.LocalDate;
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
import com.financeai.dtos.SolicitudAnalisisModeloDTO;
import com.financeai.dtos.SolicitudClasificarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.integrations.CurrencyAdapter;
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
    private final CurrencyAdapter currencyAdapter;

    public RespuestaAnalisisFinancieroDTO realizarAnalisisFinanciero(SolicitudAnalisisFinancieroDTO dto) {
        validarSolicitud(dto);

        List<TransaccionDTO> transacciones = dto.getTransacciones();
        Map<String, Double> resumenGastos = new HashMap<>();

        resumenGastos = clasificarTransaccionService.clasificarTransacciones(
                new SolicitudClasificarTransaccionesDTO(transacciones)).getClasificaciones();

        Double gastoTotal = 0.0;

        for(TransaccionDTO transaccionDTO : transacciones) {
            
            String moneda = transaccionDTO.getMoneda();

            Double rate = currencyAdapter.getConversionRate(moneda, "USD").rate();

            gastoTotal += transaccionDTO.getMonto()*rate;
        }

        String monedaIngresoMensual = dto.getMonedaIngresoMensual();

        Double rateIngresoMensual = currencyAdapter.getConversionRate(monedaIngresoMensual, "USD").rate();

        SolicitudAnalisisModeloDTO dtoModelo = new SolicitudAnalisisModeloDTO(
                dto.getFrecuenciaAhorro(),
                dto.getIngresoMensual()*rateIngresoMensual,
                dto.getNivelEndeudamiento(),
                gastoTotal
        );

        RespuestaAnalisisFinancieroDTO dtoRespuesta = modelAdapter.conectarModeloFinanceAI(
                "/api/analisis", 
                dtoModelo, 
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
    public RespuestaAnalisisFinancieroDTO realizarAnalisisFinancieroHistorico(
            LocalDate fechaInicioPeriodo,
            LocalDate fechaFinPeriodo,
            SolicitudAnalisisFinancieroHistoricoDTO dto) {
    
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) throw new ExcepcionEntidadNoEncontrada("Usuario");

        userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        String username = usuario.getUsername();

        List<Transaccion> transacciones = new ArrayList<>();

        if (fechaInicioPeriodo == null && fechaFinPeriodo == null) {
            
            transacciones = transaccionRepository.buscarTransacciones(username);

        } else if (fechaFinPeriodo == null) {

            LocalDateTime fechaHoraInicio = fechaInicioPeriodo.atStartOfDay();
            transacciones = transaccionRepository.buscarTransaccionesDesde(fechaHoraInicio, username);

        } else if (fechaInicioPeriodo == null) {

            LocalDateTime fechaHoraFin = fechaFinPeriodo.atStartOfDay().plusHours(24);
            transacciones = transaccionRepository.buscarTransaccionesHasta(fechaHoraFin, username);

        } else {

            LocalDateTime fechaHoraInicio = fechaInicioPeriodo.atStartOfDay();
            LocalDateTime fechaHoraFin = fechaFinPeriodo.atStartOfDay().plusHours(24);
            transacciones = transaccionRepository.buscarTransaccionesEntre(fechaHoraInicio, fechaHoraFin, username);

        }

        Map<String, Double> resumenGastos = new HashMap<>();
        Double gastoTotal = 0.0;

        for (Transaccion transaccion : transacciones) {

            String moneda = transaccion.getMoneda();
            Double rate = currencyAdapter.getConversionRate(moneda, "USD").rate();
            gastoTotal += transaccion.getMonto()*rate;
            resumenGastos.merge(transaccion.getCategoria(), transaccion.getMonto()*rate, Double::sum);
        }

        String monedaIngresoMensual = dto.getMonedaIngresoMensual();

        Double rateIngresoMensual = currencyAdapter.getConversionRate(monedaIngresoMensual, "USD").rate();

        SolicitudAnalisisModeloDTO dtoModelo = new SolicitudAnalisisModeloDTO(
                dto.getFrecuenciaAhorro(),
                dto.getIngresoMensual()*rateIngresoMensual,
                dto.getNivelEndeudamiento(),
                gastoTotal
        );

        RespuestaAnalisisFinancieroDTO dtoRespuesta = modelAdapter.conectarModeloFinanceAI(
                "/api/analisis", 
                dtoModelo, 
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

   
    public List<RespuestaAnalisisFinancieroDTO> obtenerHistorialAnalisisFinanciero() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) throw new ExcepcionEntidadNoEncontrada("Usuario");

        userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        List<AnalisisFinanciero> analisisFinancieros = usuario.getAnalisisFinancieros();
        List<RespuestaAnalisisFinancieroDTO> historial = new ArrayList<>();

        for (AnalisisFinanciero analisis : analisisFinancieros) {
            RespuestaAnalisisFinancieroDTO dto = new RespuestaAnalisisFinancieroDTO();
            dto.setPerfilFinanciero(analisis.getPerfilFinanciero());
            dto.setProbabilidad(analisis.getProbabilidad());
            dto.setResumenGastos(analisis.getResumenGastos());
            dto.setRecomendaciones(analisis.getRecomendaciones());
            historial.add(dto);
        }

        return historial;
    }
    
    private void validarSolicitud(SolicitudAnalisisFinancieroDTO dto) {
        if (dto.getTransacciones() == null || dto.getTransacciones().isEmpty()) {
            throw new IllegalArgumentException("La lista de transacciones no puede estar vacía");
        }
    }


}
