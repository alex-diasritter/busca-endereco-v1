package com.alex.buscacep.controller;

import com.alex.buscacep.domain.dtos.request.AuthenticationRequestDTO;
import com.alex.buscacep.domain.dtos.request.RegisterRequestDTO;
import com.alex.buscacep.infra.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequestDTO data){
        var login = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        this.authenticationManager.authenticate(login);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO data){
        return authorizationService.register(data);
    }

}