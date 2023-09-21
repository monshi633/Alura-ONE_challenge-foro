package com.alura.foro.api.domain.user;

public record UpdateUserDTO(
		String password,
		Role role,
		String firstName,
		String lastName,
		String email,
		Boolean enabled) {
}