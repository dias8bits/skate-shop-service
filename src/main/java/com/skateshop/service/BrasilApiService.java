package com.skateshop.service;

import com.skateshop.config.BrasilApiConfigurationProperties;
import com.skateshop.dto.response.CepErrorResponse;
import com.skateshop.dto.response.CepGetResponse;
import com.skateshop.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class BrasilApiService {

    private final RestClient.Builder brasilApiClient;
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
    private final ObjectMapper mapper;

    public CepGetResponse findCep(String cep) {
        return brasilApiClient.build()
                .get()
                .uri(brasilApiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var cerErrorResponse = mapper.readValue(body, CepErrorResponse.class);
                    throw new NotFoundException(cerErrorResponse.toString());
                }))
                .body(CepGetResponse.class);
    }

}
