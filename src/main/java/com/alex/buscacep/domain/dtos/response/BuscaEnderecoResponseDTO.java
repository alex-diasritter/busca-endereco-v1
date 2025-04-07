package com.alex.buscacep.domain.dtos.response;

import com.alex.buscacep.domain.dtos.request.EnderecoRequestDTO;
import com.alex.buscacep.domain.models.Busca;
import com.alex.buscacep.domain.models.Endereco;
import com.alex.buscacep.domain.models.User;

import java.time.LocalDateTime;

public class BuscaEnderecoResponseDTO {

    private LocalDateTime dataHoraBusca;
    private String username;
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String ddd;


    public BuscaEnderecoResponseDTO(Busca busca) {
        this.username = busca.getUser().getUsername();
        this.dataHoraBusca = busca.getDataHoraBusca();
        this.cep = busca.getEndereco().getCep();
        this.logradouro = busca.getEndereco().getLogradouro();
        this.bairro = busca.getEndereco().getBairro();
        this.localidade = busca.getEndereco().getLocalidade();
        this.uf = busca.getEndereco().getUf();
        this.ddd = busca.getEndereco().getDdd();
    }

    public BuscaEnderecoResponseDTO(LocalDateTime dateTime, String user, String description) {
        this.dataHoraBusca = dateTime;
        this.username = user;
    }

    public BuscaEnderecoResponseDTO(EnderecoRequestDTO enderecoDTO, User usuario) {
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

    public String getUsername() {
        return username;
    }

}