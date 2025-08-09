package com.alex.buscacep.infra.repositories;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuscaRepository extends JpaRepository<Busca, Long> {

    @Query("SELECT b FROM Busca b JOIN FETCH b.endereco")
    List<Busca> findAll();

    @Query("SELECT b FROM Busca b")
    List<Busca> findOnlyBuscas();
    
    @Query("SELECT b FROM Busca b WHERE b.user = :user")
    List<Busca> findByUser(User user);
}
