package com.alura.foro.api.domain.topic.validations.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.topic.CreateTopicDTO;
import com.alura.foro.api.domain.user.UserRepository;

import jakarta.validation.ValidationException;

@Component
public class ValidUserTopic implements CreateTopicValidators{

	@Autowired
	private UserRepository repository;
	
	@Override
	public void validate(CreateTopicDTO data) {
		var userExists = repository.existsById(data.userId());
		if (!userExists) {
			throw new ValidationException("This user doesn't exist");
		}
		
		var userEnabled = repository.findById(data.userId()).get().getEnabled();
		if (!userEnabled) {
			throw new ValidationException("This user was disabled");
		}
	}

}
