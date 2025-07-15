package com.alex.buscacep.domain.dtos.request;
import com.alex.buscacep.domain.models.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserResgistrationRequestDTO (
        @NotBlank @Size(min=3, max=50) String username,
        @NotBlank @Size(min=8) String password,
        @NotNull UserRole role) {}
