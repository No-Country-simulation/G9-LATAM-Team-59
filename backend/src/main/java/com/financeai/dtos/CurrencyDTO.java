package com.financeai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyDTO(
    @JsonProperty("iso_code")
    String isoCode,

    String name
) {}
