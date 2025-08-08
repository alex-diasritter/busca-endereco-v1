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

    @ManyToOne
    @JoinColumn(name = "user_id") // Chave estrangeira para User
    private User user;

    public Busca() {
    }

    public Busca(LocalDateTime now, Endereco endereco, User user) {
        this.dataHoraBusca = now;
        this.endereco = endereco;
        this.user = user;
    }

    public Busca(LocalDateTime now, User user) {
        this.dataHoraBusca = now;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
    }

    public Endereco getEndereco(){
        return endereco;
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