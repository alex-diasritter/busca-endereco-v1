package com.alex.buscacep.repository;

import com.alex.buscacep.entity.Buscas;
import com.alex.buscacep.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrosRepository extends JpaRepository<Buscas, String> {

    Optional<Endereco> findByCep(String cep);

}
