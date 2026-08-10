package com.financeai.services;

import org.springframework.stereotype.Service;

import com.financeai.dtos.ResponseConversionResultDTO;
import com.financeai.dtos.ResponseRateDTO;
import com.financeai.enums.AvailableCurrencies;
import com.financeai.integrations.CurrencyAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyAdapter currencyConverter;

    public ResponseRateDTO convertCurrency(AvailableCurrencies fromCurrency, AvailableCurrencies toCurrency) {
        return currencyConverter.getResponseAPI(fromCurrency.toString(), toCurrency.toString());
    }

    public ResponseConversionResultDTO convertAmount(double amount, AvailableCurrencies fromCurrency, AvailableCurrencies toCurrency) {
        ResponseRateDTO rate = currencyConverter.getResponseAPI(fromCurrency.toString(), toCurrency.toString());
        double converted = Math.round((rate.rate() * amount) * 100.0) / 100.0;
        return new ResponseConversionResultDTO(rate.date(), rate.base(), rate.quote(), rate.rate(), amount, converted);
    }

}

