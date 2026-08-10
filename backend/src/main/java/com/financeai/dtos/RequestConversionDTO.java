package com.financeai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.financeai.enums.AvailableCurrencies;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestConversionDTO {
    @NotNull(message = "amount is required") @Positive(message = "amount must be positive") Double amount;
    @NotNull(message = "fromCurrency is required") 
    @JsonProperty("from_currency")
    AvailableCurrencies fromCurrency;
    @NotNull(message = "toCurrency is required") 
    @JsonProperty("to_currency")
    AvailableCurrencies toCurrency;
}
