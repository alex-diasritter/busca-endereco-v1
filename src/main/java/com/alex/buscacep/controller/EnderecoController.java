package com.alex.buscacep.controller;

import com.alex.buscacep.dto.BuscaDTO;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/buscacep")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @GetMapping(value = "/{cep}")
    public ResponseEntity<EnderecoDTO> buscarEndereco(
            @RequestBody @PathVariable String cep) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.buscaEndereco(cep));
    }

    @GetMapping
    public ResponseEntity<List<BuscaDTO>> buscarEnderecosDb(){
        return ResponseEntity.ok(service.findAll());
    }



}
