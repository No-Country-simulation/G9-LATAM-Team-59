package com.financeai.config.exceptions;

import java.time.LocalDateTime;

public record RespuestaExcepcionDTO(
   String message,
   String error,
   LocalDateTime fechaHoraExcepcion
) {}
