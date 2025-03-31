package com.alex.buscacep.infra.service;

import com.alex.buscacep.domain.dtos.response.UserDTO;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.infra.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername acionado, buscando por usuário: {}", username);
        return repository.findByUsername(username);
    }

    @Transactional
    public UserDTO register(User user){

        var result = repository.findByUsername(user.getUsername());
        if (result != null) return new UserDTO(result.getUsername());

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        User newUser = new User(user.getUsername(), encryptedPassword, user.getRole());
        this.repository.save(newUser);
        return new UserDTO(newUser.getUsername());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> results = repository.findAll(pageable);
        log.info("Listagem obtida com sucesso.");
        return results.map(p -> new UserDTO(p.getUsername()));
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