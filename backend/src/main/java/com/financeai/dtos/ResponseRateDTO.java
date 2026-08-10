package com.financeai.dtos;

public record ResponseRateDTO(
    String date,
    String base,
    String quote,
    Double rate
) {

}