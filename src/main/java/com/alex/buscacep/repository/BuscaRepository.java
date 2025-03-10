package com.alex.buscacep.repository;

import com.alex.buscacep.entity.Busca;
import com.alex.buscacep.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuscaRepository extends JpaRepository<Busca, String> {

}
