package com.alex.buscacep.infra.repository;

import com.alex.buscacep.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Users, String> {

    UserDetails findByUsername(String username);

}
