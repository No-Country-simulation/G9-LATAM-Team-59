package com.financeai.integrations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class FinanceAiModelAdapter {

    @Value("${datascience.api.base-url:http://127.0.0.1:8000}")
    private String urlModelo;

    public <S, R> R conectarModeloFinanceAI(String endpoint, S solicitudBody, Class<R> claseRespuesta) {

        RestClient clienteHttp = RestClient.builder()
                .baseUrl(urlModelo)
                .build();

        try {
            R respuesta = clienteHttp.post()
                                .uri(endpoint)
                                .body(solicitudBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .body(claseRespuesta);
            return respuesta;

        } catch (RestClientException e) {
            throw new RuntimeException("Error en la comunicación con el modelo");
        }
    }
}
