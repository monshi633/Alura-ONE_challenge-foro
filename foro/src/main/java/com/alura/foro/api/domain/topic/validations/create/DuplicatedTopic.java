package com.alura.foro.api.domain.topic.validations.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.topic.CreateTopicDTO;
import com.alura.foro.api.domain.topic.TopicRepository;

import jakarta.validation.ValidationException;

@Component
public class DuplicatedTopic implements CreateTopicValidators{

	@Autowired
	private TopicRepository repository;
	
	@Override
	public void validate(CreateTopicDTO data) {
		var duplicatedTopic = repository.existsByTitleAndBody(data.title(), data.body());
		if (duplicatedTopic) {
			throw new ValidationException("This topic already exists. Check /topics/" + repository.findByTitle(data.title()).getId());
		}
	}

}
