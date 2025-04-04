package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnderecoRequestDTO viaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        log.info("Requisição para ViaCep montada. URL: {}", url);
        return  restTemplate.getForObject(url, EnderecoRequestDTO.class);
    }
}
