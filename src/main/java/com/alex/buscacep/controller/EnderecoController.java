package com.alex.buscacep.controller;

import com.alex.buscacep.dto.BuscaDTO;
import com.alex.buscacep.dto.EnderecoDTO;
import com.alex.buscacep.service.EnderecoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    public ResponseEntity<EnderecoDTO> buscarEndereco(
            @PathVariable
            @NotBlank(message = "O CEP é obrigatório")
            @Size(min = 8, max = 8, message = "O CEP deve ter 8 caracteres")
            @Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas números")
            String cep) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.buscaEndereco(cep));
    }

    @GetMapping
    public ResponseEntity<List<BuscaDTO>> buscarEnderecosDb(){
        return ResponseEntity.ok(service.findAll());
    }

}