package com.alex.buscacep.service;

import com.alex.buscacep.dto.EnderecoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnderecoDTO viaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, EnderecoDTO.class);
    }
}
