package com.alura.foro.api.domain.user;

public record UserDetailsDTO(
		Long id,
		String username,
		Role role,
		String firstName,
		String lastName,
		String email,
		Boolean enabled) {
	
	public UserDetailsDTO(User user) {
		this(user.getId(),
				user.getUsername(),
				user.getRole(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getEnabled());
	}
}