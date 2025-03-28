package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.repository.BuscaEUserRepository;
import com.alex.buscacep.infra.repository.BuscaRepository;
import com.alex.buscacep.infra.repository.EnderecoRepository;
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

    public BuscaEnderecoResponseDTO buscaEndereco(String cep, User usuario) throws IOException, InterruptedException {
        Optional<Endereco> enderecoDb = enderecoRepository.findByCep(cep.substring(0, 5) + "-" + cep.substring(5));
        if (enderecoDb.isPresent()) {
            return new BuscaEnderecoResponseDTO(salvarBuscaService.salvarBusca(enderecoDb.get(), usuario));
        }
        var enderecoDTO = conexaoViaCep(cep);
        Endereco enderecoNovo = new Endereco(enderecoDTO);
        enderecoRepository.save(enderecoNovo);
        return new BuscaEnderecoResponseDTO(salvarBuscaService.salvarBusca(enderecoNovo, usuario));
    }

    public List<BuscaEnderecoResponseDTO> findAll(User user){
        salvarBuscaService.salvarBusca(user);

        //buscando registros de busca atreladas à um endereço e à um user.
        List<Busca> buscas = buscaRepository.findAll();

        //buscando registros de busca atreladas à um user somente.
        List<Object[]> buscasSimples= buscaEUserRepository.buscacep();

        //junção das duas listas.
        List<BuscaEnderecoResponseDTO> result = new ArrayList<>();
        result.addAll(buscas.stream().map(BuscaEnderecoResponseDTO::new).toList());
        result.addAll(
                buscasSimples.stream()
                        .map(arr -> new BuscaEnderecoResponseDTO((LocalDateTime) arr[1], (String) arr[0],
                                "BUSCA POR LISTA DE ENDEREÇOS BUSCADOS"))
                        .toList()
        );
        System.out.println(result);
        return result;
    }

    public EnderecoRequestDTO conexaoViaCep(String cep) throws IOException, InterruptedException {
        return client.viaCep(cep);
    }
}