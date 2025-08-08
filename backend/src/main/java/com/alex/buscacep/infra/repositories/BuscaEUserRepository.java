package com.alex.buscacep.infra.repositories;

import com.alex.buscacep.domain.models.Busca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuscaEUserRepository extends JpaRepository<Busca, String> {
    @Query("SELECT u.username, b.dataHoraBusca FROM Busca b JOIN b.user u")
    List<Object[]> buscacep();
}
