package com.alex.buscacep.domain.dtos.request;

import com.alex.buscacep.domain.UserRole;

public record RegisterRequestDTO(String username, String password, UserRole role){}
