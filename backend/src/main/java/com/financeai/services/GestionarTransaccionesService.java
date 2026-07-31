package com.financeai.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financeai.config.exceptions.ExcepcionEntidadNoEncontrada;
import com.financeai.dtos.ResponseRegistrarTransaccionesDTO;
import com.financeai.dtos.TransaccionDTO;
import com.financeai.models.Transaccion;
import com.financeai.models.Usuario;
import com.financeai.repository.TransaccionRepository;
import com.financeai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestionarTransaccionesService {

    private final ClasificarTransaccionService clasificarTransaccionService;
    private final TransaccionRepository transaccionRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseRegistrarTransaccionesDTO registrarTransaccion(TransaccionDTO transaccionDTO) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) {
            throw new ExcepcionEntidadNoEncontrada("Usuario");
        }

        usuario = userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        String categoria = clasificarTransaccionService.clasificarTransaccion(transaccionDTO);

        Transaccion transaccion = new Transaccion();
        transaccion.setFechaHoraRegistro(LocalDateTime.now());
        transaccion.setDescripcion(transaccionDTO.getDescripcion());
        transaccion.setMonto(transaccionDTO.getMonto());
        transaccion.setCategoria(categoria);

        usuario.getTransacciones().add(transaccion);
        userRepository.save(usuario);

        ResponseRegistrarTransaccionesDTO respuesta = new ResponseRegistrarTransaccionesDTO();
        respuesta.setId(transaccion.getId());
        respuesta.setDescripcion(transaccion.getDescripcion());
        respuesta.setMonto(transaccion.getMonto());
        respuesta.setCategoria(transaccion.getCategoria());
        respuesta.setFechaSubida(transaccion.getFechaHoraRegistro());

        return respuesta;
    }

    public List<Transaccion> verTransaccionesRangoFecha(LocalDateTime desde, LocalDateTime hasta) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) {
            throw new ExcepcionEntidadNoEncontrada("Usuario");
        }

        usuario = userRepository.findById(usuario.getId())
                .orElseThrow(() -> new ExcepcionEntidadNoEncontrada("Usuario"));

        String username = usuario.getUsername();

        if (desde == null && hasta == null) {
            return transaccionRepository.buscarTransacciones(username);
        }

        if (desde != null && hasta != null) {
            return transaccionRepository.buscarTransaccionesEntre(desde, hasta, username);
        }

        if (desde != null) {
            return transaccionRepository.buscarTransaccionesDesde(desde, username);
        }

        if (hasta != null) {
            return transaccionRepository.buscarTransaccionesHasta(hasta, username);
        }

        return transaccionRepository.buscarTransacciones(username);
    }

    @Transactional
    public void eliminarTransaccion(Long id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (usuario == null) {
            throw new ExcepcionEntidadNoEncontrada("Usuario");
        }       
        transaccionRepository.deleteById(id);
    }
}
