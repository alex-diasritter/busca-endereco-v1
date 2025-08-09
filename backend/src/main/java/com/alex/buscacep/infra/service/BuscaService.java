package com.alex.buscacep.infra.service;
import com.alex.buscacep.domain.dtos.response.BuscaResponseDTO;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.repositories.BuscaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscaService {

    @Autowired
    private BuscaRepository buscaRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Busca salvarBusca(Endereco endereco, User user) {
        log.info("Método salvarBusca chamado.");
        var busca = new Busca(LocalDateTime.now(), endereco, user);
        buscaRepository.save(busca);
        log.info("Instância de busca com data/hora, endereço e user criada e setada corretamente e salva no banco de dados.");
        return busca;
    }

    public Busca salvarBusca(User user){
        log.info("Método salvarBusca chamado.");
        var busca = new Busca(LocalDateTime.now(), user);
        buscaRepository.save(busca);
        log.info("Instância de busca com data/hora e user criada e setada corretamente e salva no banco de dados.");
        return busca;
    }


    public List<BuscaResponseDTO> findAll(User user) {
        log.info("Requisição por histórico de buscas requisitada por usuário: {}", user.getUsername());

        // Busca apenas as buscas do usuário autenticado
        List<Busca> buscas = buscaRepository.findByUser(user);
        log.info("Histórico de {} buscas encontrado para o usuário: {}", buscas.size(), user.getUsername());

        return buscas.stream()
                .map(BuscaResponseDTO::new)
                .toList();
    }
}
