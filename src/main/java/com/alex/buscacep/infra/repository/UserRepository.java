package com.alex.buscacep.infra.repository;

import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);

    List<Busca> findByUsername(User username); // Busca todas as buscas de um usuário

}