package com.alura.foro.api.domain.user.validations.create;

import com.alura.foro.api.domain.user.CreateUserDTO;

public interface CreateUserValidators {

	public void validate(CreateUserDTO data);
	
}