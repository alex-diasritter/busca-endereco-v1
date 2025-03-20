package com.alex.buscacep.repository;

import com.alex.buscacep.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Users, String> {

    UserDetails findByUsername(String username);

}
