package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.infra.client.ViaCepClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CepService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ViaCepClient viaCepClient;

    public CepService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public EnderecoRequestDTO consultarCep(String cep) {
        log.info("Consultando CEP {} utilizando ViaCepClient.", cep);
        EnderecoRequestDTO endereco = viaCepClient.buscarEnderecoPorCep(cep);
        log.info("Endereço encontrado para o CEP {}: {}", cep, endereco);
        return endereco;
    }
}