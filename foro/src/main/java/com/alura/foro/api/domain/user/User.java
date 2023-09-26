package com.alura.foro.api.domain.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity(name = "User")
@EqualsAndHashCode(of = "id")
public class User implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean enabled;
	
	public User(CreateUserDTO createUserDTO, String hashedPassword) {
		this.username = createUserDTO.username();
		this.password = hashedPassword;
		this.role = Role.USER;
		this.firstName = capitalized(createUserDTO.firstName());
		this.lastName = capitalized(createUserDTO.lastName());
		this.email = createUserDTO.email();
		this.enabled = true;
	}

	public void updateUserWithPassword(UpdateUserDTO updateUserDTO, String hashedPassword) {
		if (updateUserDTO.password() != null) {
			this.password = hashedPassword;
		}
		if (updateUserDTO.role() != null) {
			this.role = updateUserDTO.role();
		}
		if (updateUserDTO.firstName() != null) {
			this.firstName = capitalized(updateUserDTO.firstName());
		}
		if (updateUserDTO.lastName() != null) {
			this.lastName = capitalized(updateUserDTO.lastName());
		}
		if (updateUserDTO.email() != null) {
			this.email = updateUserDTO.email();
		}
		if (updateUserDTO.enabled() != null) {
			this.enabled = updateUserDTO.enabled();
		}
	}
	
	public void updateUser(UpdateUserDTO updateUserDTO) {
		if (updateUserDTO.role() != null) {
			this.role = updateUserDTO.role();
		}
		if (updateUserDTO.firstName() != null) {
			this.firstName = capitalized(updateUserDTO.firstName());
		}
		if (updateUserDTO.lastName() != null) {
			this.lastName = capitalized(updateUserDTO.lastName());
		}
		if (updateUserDTO.email() != null) {
			this.email = updateUserDTO.email();
		}
		if (updateUserDTO.enabled() != null) {
			this.enabled = updateUserDTO.enabled();
		}
	}
	
	public void deleteUser() {
		this.enabled = false;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	private String capitalized(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
	
}