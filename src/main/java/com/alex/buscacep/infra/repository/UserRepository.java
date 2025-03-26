package com.alex.buscacep.infra.repository;

import com.alex.buscacep.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByUsername(String username);

}
