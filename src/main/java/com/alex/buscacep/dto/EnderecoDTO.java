package com.alex.buscacep.dto;

import com.alex.buscacep.entity.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnderecoDTO {
    private Long id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;

    // Construtor padrão necessário para o Jackson
    public EnderecoDTO() {
    }

    public EnderecoDTO(Optional<Endereco> end) {
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

    public Long getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
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

    public String getIbge() {
        return ibge;
    }

    public String getGia() {
        return gia;
    }

    public String getDdd() {
        return ddd;
    }

    public String getSiafi() {
        return siafi;
    }
}