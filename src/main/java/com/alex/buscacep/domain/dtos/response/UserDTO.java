package com.alex.buscacep.domain.dtos.response;

import com.alex.buscacep.domain.models.UserRole;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDTO (@NotBlank String username) {}
