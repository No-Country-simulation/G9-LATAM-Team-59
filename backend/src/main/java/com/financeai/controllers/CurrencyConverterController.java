package com.financeai.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financeai.services.CurrencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monedas")
public class CurrencyConverterController {
    
    private final CurrencyService currencyConverterService;

    @GetMapping
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok(currencyConverterService.getInfo());
    }

}
