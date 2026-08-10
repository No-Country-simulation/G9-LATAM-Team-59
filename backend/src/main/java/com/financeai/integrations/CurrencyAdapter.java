package com.financeai.integrations;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.financeai.dtos.ResponseRateDTO;

@Component
public class CurrencyAdapter {

    @Value("${frankfurter.api.base-url:https://api.frankfurter.dev}")
    private String urlAPI;

    public ResponseRateDTO getResponseAPI(String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return new ResponseRateDTO(LocalDate.now().toString(), fromCurrency, toCurrency, 1.0);
        }

        RestClient clientHttp = RestClient.builder()
                                          .baseUrl(urlAPI)
                                          .build();

        ResponseRateDTO response = clientHttp.get()
                .uri(uriBuilder -> uriBuilder
                .path("/v2/rate/{fromCurrency}/{toCurrency}")
                .build(fromCurrency, toCurrency))
                .retrieve()
                .body(ResponseRateDTO.class);

        if (response == null || response.rate() == null) {
            throw new RuntimeException("Error al consultar a la API");
        }

        return new ResponseRateDTO(response.date(), response.base(), response.quote(), response.rate());
    }

}
