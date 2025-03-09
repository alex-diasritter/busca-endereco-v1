package com.alex.buscacep.service;

import com.alex.buscacep.domain.ViaCepClient;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.repository.RegistrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnderecoService {

    private ViaCepClient client;

    public EnderecoService (ViaCepClient client){
        this.client = client;
    }

    @Autowired
    private RegistrosRepository repository;

    public EnderecoDTO conexãoViaCep(String cep) throws IOException, InterruptedException {
        EnderecoDTO dto = client.conecaoViaCep(cep);
        return dto;
    }

}
