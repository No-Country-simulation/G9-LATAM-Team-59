package com.financeai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financeai.models.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    
    @Query("""
            SELECT t FROM Usuario u
            LEFT JOIN u.transacciones t
            WHERE u.username = :username
            """)
    List<Transaccion> buscarTransacciones(@Param("username") String username);

    @Query("""
            SELECT t FROM Usuario u
            LEFT JOIN u.transacciones t
            WHERE t.fechaHoraRegistro BETWEEN :fechaHoraInicio AND :fechaHoraFin
            AND u.username = :username
            """)
    List<Transaccion> buscarTransaccionesEntre(
        @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
        @Param("fechaHoraFin") LocalDateTime fechaHoraFin,
        @Param("username") String username);

    @Query("""
            SELECT t FROM Usuario u
            LEFT JOIN u.transacciones t
            WHERE t.fechaHoraRegistro >= :fechaHoraInicio
            AND u.username = :username
            """)
    List<Transaccion> buscarTransaccionesDesde(
        @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
        @Param("username") String username);

    @Query("""
            SELECT t FROM Usuario u
            LEFT JOIN u.transacciones t
            WHERE t.fechaHoraRegistro <= :fechaHoraFin
            AND u.username = :username
            """)
    List<Transaccion> buscarTransaccionesHasta(
        @Param("fechaHoraFin") LocalDateTime fechaHoraFin,
        @Param("username") String username);
}
