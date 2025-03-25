package com.alex.buscacep.controller;

import com.alex.buscacep.domain.dtos.response.UserDTO;
import com.alex.buscacep.domain.models.User;
import com.alex.buscacep.domain.dtos.request.AuthenticationRequestDTO;
import com.alex.buscacep.domain.dtos.request.RegisterRequestDTO;
import com.alex.buscacep.domain.dtos.response.LoginResponseDTO;
import com.alex.buscacep.infra.security.TokenService;
import com.alex.buscacep.infra.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO data){
        var login = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(login);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterRequestDTO data){
        var user = authorizationService.register(data);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> list(Pageable pageable){
        return ResponseEntity.ok(authorizationService.findAll(pageable));
    }


    //Está retornando 403 e

    @DeleteMapping("/{username}")
    public ResponseEntity delete(@PathVariable String username){
        var result = authorizationService.delete(username);
        if (result) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

}