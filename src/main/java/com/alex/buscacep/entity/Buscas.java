package com.alex.buscacep.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_buscas")
public class Buscas {

    @Id
    private Long id;

    private LocalDateTime dataHoraBusca;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Buscas() {
    }

    public Buscas(Long id, LocalDateTime localDateTime) {
        this.id = id;
        this.dataHoraBusca = localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
    }

    public void setDataHoraBusca(LocalDateTime dataHoraBusca) {
        this.dataHoraBusca = dataHoraBusca;
    }
}