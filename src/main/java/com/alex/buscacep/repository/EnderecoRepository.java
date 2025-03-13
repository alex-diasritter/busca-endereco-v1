package com.alex.buscacep.repository;

import com.alex.buscacep.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM TB_ENDERECOS
            WHERE CEP = :cep
            """)
    Optional<Endereco> findByCep(String cep);

}
