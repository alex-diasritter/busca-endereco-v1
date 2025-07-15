package com.alex.buscacep.infra.service;
import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.repositories.EnderecoRepository;
import com.alex.buscacep.infra.service.exceptions.CepNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private CepService client;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BuscaService salvarBuscaService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public BuscaEnderecoResponseDTO buscaEndereco(String cep, User usuario) {
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
            throw new CepNotFoundException("Nenhum endereco para o cep: " + cep);
        }
        log.info("Conexão com ViaCep bem sucedida");
        log.info("Endereço encontrado.");
        Endereco enderecoNovo = new Endereco(enderecoDTO);
        enderecoRepository.save(enderecoNovo);
        log.info("EnderecoDTO convertido para Endereco e salvo no banco de dados com sucesso.");
        return new BuscaEnderecoResponseDTO(salvarBuscaService.salvarBusca(enderecoNovo, usuario));
    }

    public List<Endereco> findAll(User user){
        salvarBuscaService.salvarBusca(user);
        log.info("Busca por listagem de endereços requisitada por usuário: {} foi salva no DB.", user.getUsername());

        List<Endereco> enderecos = enderecoRepository.findAll();
        log.info("Busca por todos os endereços realizada.");

        return enderecos;
    }

    public EnderecoRequestDTO conexaoViaCep(String cep) {
        return client.consultarCep(cep);
    }
}