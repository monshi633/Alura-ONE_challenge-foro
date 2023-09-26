package com.alura.foro.api.domain.user.validations.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.user.UpdateUserDTO;
import com.alura.foro.api.domain.user.UserRepository;

import jakarta.validation.ValidationException;

@Component
public class UpdateValidUser implements UpdateUserValidators{

	@Autowired
	private UserRepository repository;
	
	@Override
	public void validate(UpdateUserDTO data) {
		if (data.email() != null ) {
			
			var duplicatedEmail = repository.findByEmail(data.email());
			if (duplicatedEmail != null) {
				throw new ValidationException("This email is already in use");
			}
			
		}
	}

}