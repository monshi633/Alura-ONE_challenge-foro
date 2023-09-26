package com.alura.foro.api.domain.answer;

import java.time.LocalDateTime;

public record AnswerDetailsDTO (
		Long id,
		String body,
		LocalDateTime creationDate,
		LocalDateTime lastUpdate,
		Boolean solution,
		Boolean deleted,
		Long userId,
		String username,
		Long topicId,
		String topic
		) {
	
	public AnswerDetailsDTO(Answer answer) {
		this(
				answer.getId(),
				answer.getBody(),
				answer.getCreationDate(),
				answer.getLastUpdated(),
				answer.getSolution(),
				answer.getDeleted(),
				answer.getUser().getId(),
				answer.getUser().getUsername(),
				answer.getTopic().getId(),
				answer.getTopic().getTitle());
	}

}