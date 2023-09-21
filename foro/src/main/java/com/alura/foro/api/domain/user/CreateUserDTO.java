package com.alura.foro.api.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(@NotBlank String username, @NotBlank String password, @NotNull Role role,
		@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email) {
}