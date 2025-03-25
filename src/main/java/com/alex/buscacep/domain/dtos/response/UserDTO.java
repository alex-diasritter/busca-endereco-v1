package com.alex.buscacep.domain.dtos.response;

import com.alex.buscacep.domain.models.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDTO (String username) {}
