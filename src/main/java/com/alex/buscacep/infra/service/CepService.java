package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnderecoRequestDTO viaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, EnderecoRequestDTO.class);
    }
}
