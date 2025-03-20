package com.alex.buscacep.infra.repository;

import com.alex.buscacep.domain.Busca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuscaRepository extends JpaRepository<Busca, String> {

    @Query(value = "SELECT b FROM Busca b JOIN FETCH b.endereco")
    List<Busca> findAll();


}
