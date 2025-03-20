package com.alex.buscacep.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(@NotBlank(message = "Username cannot be null") String username,
                                       @NotBlank(message = "Password cannot be null") String password){}