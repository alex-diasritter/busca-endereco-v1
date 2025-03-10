package com.alex.buscacep.dto;

import com.alex.buscacep.entity.Busca;
import com.alex.buscacep.entity.Endereco;

import java.time.LocalDateTime;

public class BuscaDTO {

    private Long id;
    private LocalDateTime dataHoraBusca;
    private Endereco endereco;

    public BuscaDTO() {
    }

    public BuscaDTO(Long id, LocalDateTime dataHoraBusca, Endereco endereco) {
        this.id = id;
        this.dataHoraBusca = dataHoraBusca;
        this.endereco = endereco;
    }

    public BuscaDTO(Busca busca) {
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
