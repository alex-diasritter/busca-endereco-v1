package com.alex.buscacep.infra.repository;

import com.alex.buscacep.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findByCep(String cep);

}