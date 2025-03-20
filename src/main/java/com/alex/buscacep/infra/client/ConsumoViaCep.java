package com.alex.buscacep.infra.client;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsumoViaCep {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnderecoRequestDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), EnderecoRequestDTO.class);
    }
}