package com.alura.foro.api.domain.user.validations.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.user.CreateUserDTO;
import com.alura.foro.api.domain.user.UserRepository;

import jakarta.validation.ValidationException;

@Component
public class DuplicatedUser implements CreateUserValidators{

	@Autowired
	private UserRepository repository;
	
	@Override
	public void validate(CreateUserDTO data) {
		var duplicatedUser = repository.findByUsername(data.username());
		if (duplicatedUser != null) {
			throw new ValidationException("This user already exists");
		}
		
		var duplicatedEmail = repository.findByEmail(data.email());
		if (duplicatedEmail != null) {
			throw new ValidationException("This email is already in use");
		}
	}
}
