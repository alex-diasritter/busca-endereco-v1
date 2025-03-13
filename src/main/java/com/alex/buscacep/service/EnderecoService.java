package com.alex.buscacep.service;

import com.alex.buscacep.config.ConsumoViaCep;
import com.alex.buscacep.dto.BuscaDTO;
import com.alex.buscacep.dto.BuscaEnderecoResponseDTO;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.entity.Busca;
import com.alex.buscacep.entity.Endereco;
import com.alex.buscacep.repository.BuscaRepository;
import com.alex.buscacep.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private ConsumoViaCep client;

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    //criar metodo para registrar busca

    public BuscaEnderecoResponseDTO buscaEndereco (String cep) throws IOException, InterruptedException {

        Busca busca = new Busca();

        Optional<Endereco> enderecoDb = enderecoRepository.findByCep(cep);
        if (enderecoDb.isPresent()){
            busca.setDataHoraBusca(LocalDateTime.now());
            busca.setEndereco(enderecoDb.get());
            buscaRepository.save(busca);
            return new BuscaEnderecoResponseDTO(busca);

        } else {

            var enderecoDTO = conexaoViaCep(cep);
            Endereco enderecoNovo = new Endereco(enderecoDTO);
            enderecoRepository.save(enderecoNovo);

            busca.setDataHoraBusca(LocalDateTime.now());
            busca.setEndereco(enderecoNovo);
            buscaRepository.save(busca);

            return new BuscaEnderecoResponseDTO(busca);
        }
    }
    //org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Unique index or primary key violation: "PUBLIC.CONSTRAINT_INDEX_B ON PUBLIC.TB_ENDERECOS(CEP NULLS FIRST) VALUES ( /* 1 */ '24900-435' )"; SQL statement:


    public List<BuscaEnderecoResponseDTO> findAll(){
        List<Busca> buscas = buscaRepository.findAll();
        List<BuscaEnderecoResponseDTO> dtoResponse = buscas.stream()
                .map(BuscaEnderecoResponseDTO::new)
                .collect(Collectors.toList());
        return dtoResponse;
    }

    public EnderecoDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        return client.conexaoViaCep(cep);
    }

}