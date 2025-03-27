package com.alex.buscacep.domain.dtos.request;

import jakarta.validation.constraints.NotEmpty;

public record UserLogin(@NotEmpty(message = "Username não pode ser vazio") String username,
                        @NotEmpty(message = "Passwprd não pode ser vazia") String password){}