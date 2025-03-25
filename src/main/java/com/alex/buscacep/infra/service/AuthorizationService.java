package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.response.UserDTO;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.domain.dtos.request.RegisterRequestDTO;
import com.alex.buscacep.infra.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public UserDTO register(RegisterRequestDTO registerDTO){

        var result = repository.findByUsername(registerDTO.username());
        if (result != null) return new UserDTO(result.getUsername(), result.getAuthorities());

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User newUser = new User(registerDTO.username(), encryptedPassword, registerDTO.role());
        this.repository.save(newUser);
        return new UserDTO(newUser.getUsername(), newUser.getAuthorities());
    }


    //Está retornando 403 e não consegui resolver
    /*
    @Transactional
    public boolean delete(String id) {
        Optional<User> usuario = repository.findById(id);
        if (usuario.isPresent()) {
            repository.delete(usuario.get());
            return true;
        }
        return false;
    }
     */

}