package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.Users;
import com.alex.buscacep.domain.dtos.request.RegisterRequestDTO;
import com.alex.buscacep.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public ResponseEntity register( RegisterRequestDTO registerDTO){
        if (this.repository.findByUsername(registerDTO.username()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        Users newUser = new Users(registerDTO.username(), encryptedPassword, registerDTO.role());
        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }

}
