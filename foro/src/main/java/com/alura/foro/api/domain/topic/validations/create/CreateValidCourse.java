package com.alura.foro.api.domain.topic.validations.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.course.CourseRepository;
import com.alura.foro.api.domain.topic.CreateTopicDTO;

import jakarta.validation.ValidationException;

@Component
public class CreateValidCourse implements CreateTopicValidators{

	@Autowired
	private CourseRepository repository;
	
	@Override
	public void validate(CreateTopicDTO data) {
		
		var courseExists = repository.existsById(data.courseId());
		if (!courseExists) {
			throw new ValidationException("This course doesn't exist");
		}
		
		var courseEnabled = repository.findById(data.courseId()).get().getActive();
		if (!courseEnabled) {
			throw new ValidationException("This course is not able at the moment");
		}

	}

}