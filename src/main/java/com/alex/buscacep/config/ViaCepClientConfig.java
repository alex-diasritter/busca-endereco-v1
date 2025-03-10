package com.alex.buscacep.config;

import com.alex.buscacep.dto.EnderecoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public class ViaCepClientConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnderecoDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), EnderecoDTO.class);
    }
}