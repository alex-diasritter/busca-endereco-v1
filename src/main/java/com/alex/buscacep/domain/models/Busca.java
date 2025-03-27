package com.alex.buscacep.domain.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_buscas")
public class Busca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraBusca;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Busca() {
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
    }

    public void setDataHoraBusca(LocalDateTime dataHoraBusca) {
        this.dataHoraBusca = dataHoraBusca;
    }

    public Endereco getEndereco(){
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Busca busca = (Busca) o;
        return Objects.equals(endereco, busca.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endereco);
    }
}