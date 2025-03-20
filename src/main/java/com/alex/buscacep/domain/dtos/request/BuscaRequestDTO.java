package com.alex.buscacep.domain.dtos.request;

import com.alex.buscacep.domain.Busca;
import com.alex.buscacep.domain.Endereco;

import java.time.LocalDateTime;

public class BuscaRequestDTO {

    private Long id;
    private LocalDateTime dataHoraBusca;
    private Endereco endereco;

    public BuscaRequestDTO() {
    }

    public BuscaRequestDTO(Long id, LocalDateTime dataHoraBusca, Endereco endereco) {
        this.id = id;
        this.dataHoraBusca = dataHoraBusca;
        this.endereco = endereco;
    }

    public BuscaRequestDTO(Busca busca) {
        this.id = getId();
        this.dataHoraBusca = getDataHoraBusca();
        this.endereco = getEndereco();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
