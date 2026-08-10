package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financeai.enums.AvailableCurrencies;
import com.financeai.dtos.ResponseRateDTO;
import com.financeai.services.CurrencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyConverterController {
    
    private final CurrencyService currencyConverterService;

    @GetMapping("/info")
    public ResponseEntity<ResponseRateDTO> convertirMoneda(
            @RequestParam("fromCurrency") AvailableCurrencies fromCurrency,
            @RequestParam("toCurrency") AvailableCurrencies toCurrency) {
        return ResponseEntity.ok(currencyConverterService.convertCurrency(fromCurrency, toCurrency));
    }

    @GetMapping("/convertir")
    public ResponseEntity<com.financeai.dtos.ResponseConversionResultDTO> convertirMonto(
            @RequestParam("amount") double amount,
            @RequestParam("fromCurrency") AvailableCurrencies fromCurrency,
            @RequestParam("toCurrency") AvailableCurrencies toCurrency) {
        return ResponseEntity.ok(currencyConverterService.convertAmount(amount, fromCurrency, toCurrency));
    }
}
