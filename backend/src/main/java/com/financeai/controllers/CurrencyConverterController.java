package com.financeai.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import com.financeai.enums.AvailableCurrencies;
import com.financeai.dtos.ResponseRateDTO;
import com.financeai.dtos.RequestConversionDTO;
import com.financeai.dtos.RequestRateDTO;
import com.financeai.dtos.ResponseConversionResultDTO;
import com.financeai.services.CurrencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyConverterController {
    
    private final CurrencyService currencyConverterService;

    @PostMapping("/info")
    public ResponseEntity<ResponseRateDTO> getInfo(
            @RequestBody @Valid RequestRateDTO request) {
        return ResponseEntity.ok(currencyConverterService.convertCurrency(request.getFromCurrency(), request.getToCurrency()));
    }

    // @GetMapping("/convertir")
    // public ResponseEntity<ResponseConversionResultDTO> convertirMonto(
    //         @RequestParam("amount") double amount,
    //         @RequestParam("fromCurrency") AvailableCurrencies fromCurrency,
    //         @RequestParam("toCurrency") AvailableCurrencies toCurrency) {
    //     return ResponseEntity.ok(currencyConverterService.convertAmount(amount, fromCurrency, toCurrency));
    // }

    @PostMapping("/convertir")
    public ResponseEntity<ResponseConversionResultDTO> convertirMontoJson(@RequestBody @Valid RequestConversionDTO request) {
        return ResponseEntity.ok(currencyConverterService.convertAmount(request.getAmount(), request.getFromCurrency(), request.getToCurrency()));
    }
}
