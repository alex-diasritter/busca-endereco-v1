package com.alex.buscacep.controller;

import com.alex.buscacep.domain.dtos.response.BuscaEnderecoResponseDTO;
import com.alex.buscacep.infra.service.EnderecoService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/buscacep")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @GetMapping(value = "/{cep}")
    public ResponseEntity<BuscaEnderecoResponseDTO> buscarEndereco(
            @PathVariable @Size(min = 8, max = 8, message = "O CEP deve ter apenas 8 números") String cep)
            throws IOException, InterruptedException {

        return ResponseEntity.ok(service.buscaEndereco(cep));
    }

    @GetMapping
    public ResponseEntity<List<BuscaEnderecoResponseDTO>> buscarEnderecosDb(){
        return ResponseEntity.ok(service.findAll());
    }

}