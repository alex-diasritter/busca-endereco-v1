package com.alex.buscacep.repository;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.Endereco;
import com.alex.buscacep.infra.repository.EnderecoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EnderecoRepositoryTest {

    String cep = "29300-849";

    @Autowired
    EntityManager entityManager;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Test
    @DisplayName("Should get Endereco succesfully from DB")
    void findByCepCase1() {
        LocalDateTime agora = LocalDateTime.now();
        EnderecoRequestDTO e = new EnderecoRequestDTO("29300849", "Rua dos bobos", "Bobões",
                "BoboCity", "BC", "00");
        createEndereco(e);
        Optional<Endereco> result = this.enderecoRepository.findByCep("29300849");
        assertEquals(true, result.isPresent());
    }

    @Test
    @DisplayName("Should not get Endereco succesfully from DB")
    void findByCepCase2() {
        Optional<Endereco> result = this.enderecoRepository.findByCep("29300849");
        assertFalse(result.isPresent());
    }

    private Endereco createEndereco(EnderecoRequestDTO e){
        Endereco endereco = new Endereco(e);
        this.entityManager.persist(endereco);
        this.entityManager.flush();
        return endereco;
    }
}