package com.alex.buscacep.domain.models;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "tb_enderecos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 9)
    @JsonProperty("cep")
    private String cep;

    @JsonProperty("logradouro")
    private String logradouro;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("localidade")
    private String localidade;

    @JsonProperty("uf")
    private String uf;

    @JsonProperty("ddd")
    private String ddd;

    public Endereco() {
    }

    public Endereco(EnderecoRequestDTO end) {
        this.cep = end.getCep();
        this.logradouro = end.getLogradouro();
        this.bairro = end.getBairro();
        this.localidade = end.getLocalidade();
        this.uf = end.getUf();
        this.ddd = end.getDdd();
    }

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    private List<Busca> buscas = new ArrayList<>();

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getUf() {
        return uf;
    }

    public String getDdd() {
        return ddd;
    }

    public Long getId() {
        return id;
    }

    // toString para facilitar debug
    @Override
    public String toString() {
        return "Endereco{" +
                "cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", bairro='" + bairro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cep);
    }
}