package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.infra.client.ViaCepClient;
import com.alex.buscacep.infra.service.exceptions.CepNotFoundException;
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
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException(
                    "CEP deve conter apenas 8 dígitos numéricos"
            );
        }
        if (!cepLimpo.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "CEP deve conter apenas 8 dígitos numéricos"
            );
        }
        EnderecoRequestDTO endereco = viaCepClient.buscarEnderecoPorCep(cep);
        if (endereco == null) {
            log.warn("Cep não encontrado");
            throw new CepNotFoundException("Nenhum endereço para o cep: " + cep);
        }
        log.info("Endereço encontrado para o CEP {}: {}", cep, endereco);
        return endereco;
    }
}