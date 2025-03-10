package com.alex.buscacep.service;

import com.alex.buscacep.config.ViaCepClientConfig;
import com.alex.buscacep.dto.BuscaDTO;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.entity.Busca;
import com.alex.buscacep.entity.Endereco;
import com.alex.buscacep.repository.BuscaRepository;
import com.alex.buscacep.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    private ViaCepClientConfig client;

    public EnderecoService (ViaCepClientConfig client){
        this.client = client;
    }

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public EnderecoDTO buscaEndereco (String cep) throws IOException, InterruptedException {

        var endereco = enderecoRepository.findByCep(cep);
        if (endereco.isPresent()) { return new EnderecoDTO(endereco); }

        Busca busca = new Busca();
        busca.setDataHoraBusca(LocalDateTime.now());
        buscaRepository.save(busca);

        var enderecoDTO = conexaoViaCep(cep);
        var enderecoNovo = new Endereco(enderecoDTO);
        enderecoRepository.save(enderecoNovo);
        return enderecoDTO;
    }

    public EnderecoDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        return client.conexaoViaCep(cep);
    }

    public List<BuscaDTO> findAll(){
        List<Busca> buscas = buscaRepository.findAll();
        List<BuscaDTO> buscasDto = buscas.stream()
                .map(BuscaDTO::new)
                .collect(Collectors.toList());
        return buscasDto;
    }
}