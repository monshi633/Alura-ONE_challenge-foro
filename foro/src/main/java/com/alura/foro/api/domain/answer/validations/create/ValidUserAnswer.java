package com.alura.foro.api.domain.answer.validations.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.answer.CreateAnswerDTO;
import com.alura.foro.api.domain.user.UserRepository;

import jakarta.validation.ValidationException;

@Component
public class ValidUserAnswer implements CreateAnswerValidators{

	@Autowired
	private UserRepository repository;

//	@Autowired
//	private TopicRepository topicRepository;	
	
	@Override
	public void validate(CreateAnswerDTO data) {
		
		var userExists = repository.existsById(data.userId());

		if (!userExists) {
			throw new ValidationException("This user doesn't exist");
		}
		
		var userEnabled = repository.findById(data.userId()).get().isEnabled();
		
		if (!userEnabled) {
			throw new ValidationException("This user isn't enabled");
		}
		
//		var topicExists = topicRepository.existsById(data.topicId());
		//topic open
//		if (!courseEnabled) {
//			throw new ValidationException("This course is not able at the moment");
//		}

	}

}