package com.alex.buscacep.domain.dtos.response;

import com.alex.buscacep.domain.models.Busca;

import java.time.LocalDateTime;

public class BuscaResponseDTO {

    private LocalDateTime dataHoraBusca;
    private String usersame;

    public BuscaResponseDTO() {
    }

    public BuscaResponseDTO(Busca busca) {
        this.dataHoraBusca = busca.getDataHoraBusca();
        this.usersame = busca.getUser().getUsername();
    }

    public LocalDateTime getDataHoraBusca() {
        return dataHoraBusca;
    }

    public String getUsersame() {
        return usersame;
    }
}
