package com.alura.foro.api.domain.answer.validations.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.foro.api.domain.answer.Answer;
import com.alura.foro.api.domain.answer.AnswerRepository;
import com.alura.foro.api.domain.answer.UpdateAnswerDTO;
import com.alura.foro.api.domain.topic.Status;
import com.alura.foro.api.domain.topic.TopicRepository;

import jakarta.validation.ValidationException;

@Component
public class DuplicatedSolution implements UpdateAnswerValidators {

	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Override
	public void validate(UpdateAnswerDTO data, Long answerId) {
		if (data.solution()) {
			Answer answer = answerRepository.getReferenceById(answerId);
			var solvedTopic = topicRepository.getReferenceById(answer.getTopic().getId());
			if (solvedTopic.getStatus() == Status.CLOSED) {
				throw new ValidationException("This topic is already solved");
			}
		}
		
		
	}

}
