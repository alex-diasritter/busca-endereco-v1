package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.repositories.BuscaEUserRepository;
import com.alex.buscacep.infra.repositories.BuscaRepository;
import com.alex.buscacep.infra.repositories.EnderecoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private CepService client;

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private BuscaEUserRepository buscaEUserRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private SalvarBuscaService salvarBuscaService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public BuscaEnderecoResponseDTO buscaEndereco(String cep, User usuario) throws IOException, InterruptedException {
        Optional<Endereco> enderecoDb = enderecoRepository.findByCep(cep.substring(0, 5) + "-" + cep.substring(5));
        if (enderecoDb.isPresent()) {
            log.info("Endereço encontrado nos registros locais. Não há necessidade de chamar ViaCep.");
            return new BuscaEnderecoResponseDTO(salvarBuscaService.salvarBusca(enderecoDb.get(), usuario));
        }
        log.info("Endereço não registrado localmente, tentativa de conexão à ViaCep inicializada.");
        var enderecoDTO = conexaoViaCep(cep);
        if (enderecoDTO.getCep() == null) {
            log.info("Conexão com ViaCep bem sucedida");
            log.info("Endereço não encontrado.");
            return new BuscaEnderecoResponseDTO(enderecoDTO, usuario);
        }
        log.info("Conexão com ViaCep bem sucedida");
        log.info("Endereço encontrado.");
        Endereco enderecoNovo = new Endereco(enderecoDTO);
        enderecoRepository.save(enderecoNovo);
        log.info("EnderecoDTO convertido para Endereco e salvo no banco de dados com sucesso.");
        return new BuscaEnderecoResponseDTO(salvarBuscaService.salvarBusca(enderecoNovo, usuario));
    }

    public List<BuscaEnderecoResponseDTO> findAll(User user){
        salvarBuscaService.salvarBusca(user);
        log.info("Busca por listagem de endereços requisitada por usuário: {} foi salva no DB.", user.getUsername());

        List<Busca> buscas = buscaRepository.findAll();
        log.info("Busca por todos os registros de buscas atreladas à um endereço e a um usuário realizada.");

        List<Object[]> userBuscas = buscaEUserRepository.buscacep();
        log.info("Busca por todos os registros de buscas feitas por um usuário realizada.");

        List<BuscaEnderecoResponseDTO> result = new ArrayList<>();
        result.addAll(buscas.stream().map(BuscaEnderecoResponseDTO::new).toList());
        result.addAll(
                userBuscas.stream()
                        .map(arr -> new BuscaEnderecoResponseDTO((LocalDateTime) arr[1], (String) arr[0],
                                "BUSCA POR LISTA DE ENDEREÇOS BUSCADOS"))
                        .toList()

        );
        log.info("Junção da lista - buscas e - userBuscas realizada com sucesso.");
        return result;
    }

    public EnderecoRequestDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        log.info("Método para buscar endereço acionado, argumento cep: {}", cep);
        return client.viaCep(cep);
    }
}