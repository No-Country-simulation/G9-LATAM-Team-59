package com.financeai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.financeai.enums.AvailableCurrencies;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class RequestRateDTO {  
    @NotNull(message = "fromCurrency is required")
    @JsonProperty("from_currency") AvailableCurrencies fromCurrency;
    @NotNull(message = "toCurrency is required")
    @JsonProperty("to_currency") AvailableCurrencies toCurrency;
}
