package com.alex.buscacep.controller;

import com.alex.buscacep.domain.ViaCepClient;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/buscacep")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @GetMapping(value = "/{cep}")
    public ResponseEntity<EnderecoDTO> buscarEndereco(@RequestBody @PathVariable String cep) throws IOException, InterruptedException {
    EnderecoDTO dto = service.conexãoViaCep(cep);
        return ResponseEntity.ok(dto);
    }
}
