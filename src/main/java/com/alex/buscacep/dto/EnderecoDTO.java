package com.alex.buscacep.dto;

import com.alex.buscacep.entity.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnderecoDTO {

    @NotBlank(message = "O CEP é obrigatório")
    @Size(min = 8, max = 8, message = "Deve conter 8 números")
    private String cep;

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String ddd;

    public EnderecoDTO() {
    }

    public EnderecoDTO(Optional<Endereco> end) {
        this.cep = end.get().getCep();
        this.logradouro = end.get().getLogradouro();
        this.bairro = end.get().getBairro();
        this.localidade = end.get().getLocalidade();
        this.uf = end.get().getUf();
        this.ddd = end.get().getDdd();
    }

    public EnderecoDTO(Endereco end) {
        this.cep = end.getCep();
        this.logradouro = end.getLogradouro();
        this.bairro = end.getBairro();
        this.localidade = end.getLocalidade();
        this.uf = end.getUf();
        this.ddd = end.getDdd();
    }

    public EnderecoDTO(String cep, String logradouro, String bairro, String localidade, String uf, String ddd) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.ddd = ddd;
    }

    public String getCep() {
        return cep;
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

    @Override
    public String toString() {
        return "EnderecoDTO{" +
                "cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", bairro='" + bairro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                ", ddd='" + ddd + '\'' +
                '}';
    }
}