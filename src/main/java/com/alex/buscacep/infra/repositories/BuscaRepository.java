package com.alex.buscacep.infra.repositories;

import com.alex.buscacep.domain.models.Busca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuscaRepository extends JpaRepository<Busca, Long> {

    @Query("SELECT b FROM Busca b JOIN FETCH b.endereco")
    List<Busca> findAll();

}
