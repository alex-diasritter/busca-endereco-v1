package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.infra.repository.BuscaRepository;
import com.alex.buscacep.infra.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private CepService client;

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    private Busca salvarBusca(Endereco endereco){
        Busca busca = new Busca();
        busca.setDataHoraBusca(LocalDateTime.now());
        busca.setEndereco(endereco);
        buscaRepository.save(busca);
        return busca;
    }

    public BuscaEnderecoResponseDTO buscaEndereco (String cep) throws IOException, InterruptedException {

        Optional<Endereco> enderecoDb = enderecoRepository.findByCep(cep.substring(0, 5) + "-" + cep.substring(5));
        if (enderecoDb.isPresent()) {
            return new BuscaEnderecoResponseDTO(salvarBusca(enderecoDb.get()));
        }

        var enderecoDTO = conexaoViaCep(cep);
        Endereco enderecoNovo = new Endereco(enderecoDTO);
        enderecoRepository.save(enderecoNovo);
        return new BuscaEnderecoResponseDTO(salvarBusca(enderecoNovo));
    }

    public List<BuscaEnderecoResponseDTO> findAll(){
        List<Busca> buscas = buscaRepository.findAll();
        List<BuscaEnderecoResponseDTO> dtoResponse = buscas.stream()
                .map(BuscaEnderecoResponseDTO::new)
                .collect(Collectors.toList());
        return dtoResponse;
    }

    public EnderecoRequestDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        return client.viaCep(cep);
    }

}