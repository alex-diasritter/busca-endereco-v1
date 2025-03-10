package com.alex.buscacep.entity;

import com.alex.buscacep.dto.EnderecoDTO;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "tb_enderecos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // Garante que cada CEP seja único no banco
    @JsonProperty("cep")
    private String cep;

    @JsonProperty("logradouro")
    private String logradouro;

    @JsonProperty("complemento")
    private String complemento;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("localidade")
    private String localidade;

    @JsonProperty("uf")
    private String uf;

    @JsonProperty("ibge")
    private String ibge;

    @JsonProperty("gia")
    private String gia;

    @JsonProperty("ddd")
    private String ddd;

    @JsonProperty("siafi")
    private String siafi;

    // Construtor padrão necessário para desserialização do Jackson
    public Endereco() {
    }


    public Endereco(EnderecoDTO end) {
        this.id = end.getId();
        this.cep = end.getCep();
        this.logradouro = end.getLogradouro();
        this.complemento = end.getComplemento();
        this.bairro = end.getBairro();
        this.localidade = end.getLocalidade();
        this.uf = end.getUf();
        this.ibge = end.getIbge();
        this.gia = end.getGia();
        this.ddd = end.getDdd();
        this.siafi = end.getSiafi();
    }

    public Endereco(Optional<Endereco> end) {
        this.id = end.get().getId();
        this.cep = end.get().getCep();
        this.logradouro = end.get().getLogradouro();
        this.complemento = end.get().getComplemento();
        this.bairro = end.get().getBairro();
        this.localidade = end.get().getLocalidade();
        this.uf = end.get().getUf();
        this.ibge = end.get().getIbge();
        this.gia = end.get().getGia();
        this.ddd = end.get().getDdd();
        this.siafi = end.get().getSiafi();
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

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getSiafi() {
        return siafi;
    }

    public void setSiafi(String siafi) {
        this.siafi = siafi;
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
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                '}';
    }
}
