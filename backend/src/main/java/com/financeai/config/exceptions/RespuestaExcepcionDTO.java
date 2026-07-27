package com.financeai.config.exceptions;

import java.time.LocalDateTime;

public record RespuestaExcepcionDTO(
   String mensaje,
   String error,
   LocalDateTime fechaHoraExcepcion
) {}
