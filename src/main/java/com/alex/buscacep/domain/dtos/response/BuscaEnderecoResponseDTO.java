package com.alex.buscacep.domain.dtos.response;

import com.alex.buscacep.domain.models.Busca;

import java.time.LocalDateTime;

public class BuscaEnderecoResponseDTO {

    private LocalDateTime dataHoraBusca;
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String ddd;

    public BuscaEnderecoResponseDTO() {
    }

    public BuscaEnderecoResponseDTO(LocalDateTime dataHoraBusca, String cep, String logradouro, String bairro, String localidade, String uf, String ddd) {
        this.dataHoraBusca = dataHoraBusca;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.ddd = ddd;
    }

    public BuscaEnderecoResponseDTO(Busca busca) {
        this.dataHoraBusca = busca.getDataHoraBusca();
        this.cep = busca.getEndereco().getCep();
        this.logradouro = busca.getEndereco().getLogradouro();
        this.bairro = busca.getEndereco().getBairro();
        this.localidade = busca.getEndereco().getLocalidade();
        this.uf = busca.getEndereco().getUf();
        this.ddd = busca.getEndereco().getDdd();
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
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
}
