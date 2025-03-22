package com.alex.buscacep.service;

import com.alex.buscacep.domain.busca.Busca;
import com.alex.buscacep.domain.endereco.Endereco;
import com.alex.buscacep.repository.BuscaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SalvarBuscaService {

    @Autowired
    private BuscaRepository buscaRepository;

    public Busca salvarBusca(Endereco endereco) {
        var busca = new Busca();
        busca.setDataHoraBusca(LocalDateTime.now());
        busca.setEndereco(endereco);
        buscaRepository.save(busca);
        return busca;
    }
}
