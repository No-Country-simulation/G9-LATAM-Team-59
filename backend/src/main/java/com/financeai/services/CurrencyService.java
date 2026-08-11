package com.financeai.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.financeai.dtos.CurrencyDTO;
import com.financeai.integrations.CurrencyAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyAdapter currencyAdapter;

    public List<CurrencyDTO> getInfo() {
        List<CurrencyDTO> currencies = currencyAdapter.getCurrencies();
        return currencies;     
    }

}

