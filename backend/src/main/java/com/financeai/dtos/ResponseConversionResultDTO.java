package com.financeai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseConversionResultDTO(
    String date,
    String base,
    String quote,
    Double rate,
    Double amount,
    @JsonProperty("converted_amount")
    Double convertedAmount
) {

}
