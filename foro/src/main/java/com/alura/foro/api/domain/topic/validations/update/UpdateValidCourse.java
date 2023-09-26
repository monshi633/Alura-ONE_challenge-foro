package com.alura.foro.api.domain.topic.validations.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.course.CourseRepository;
import com.alura.foro.api.domain.topic.UpdateTopicDTO;

import jakarta.validation.ValidationException;

@Component
public class UpdateValidCourse implements UpdateTopicValidators{

	@Autowired
	private CourseRepository repository;
	
	@Override
	public void validate(UpdateTopicDTO data) {
		if (data.courseId() != null ) {
			
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

}