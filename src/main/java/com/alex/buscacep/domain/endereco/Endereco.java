package com.alex.buscacep.domain.endereco;

import com.alex.buscacep.domain.busca.Busca;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity(name = "endereco")
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

    public Endereco(Optional<Endereco> end) {
        this.id = end.get().getId();
        this.cep = end.get().getCep();
        this.logradouro = end.get().getLogradouro();
        this.bairro = end.get().getBairro();
        this.localidade = end.get().getLocalidade();
        this.uf = end.get().getUf();
        this.ddd = end.get().getDdd();
    }

    public Endereco(String cep) {
        this.cep = cep;
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

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }


    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }


    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public Long getId() {
        return id;
    }

    public List<Busca> getBuscas() {
        return buscas;
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