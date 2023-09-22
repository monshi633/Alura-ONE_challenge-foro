package com.alura.foro.api.domain.answer;

import java.time.LocalDateTime;

public record AnswerDetailsDTO (
		Long id,
		String body,
		LocalDateTime creationDate,
		LocalDateTime lastUpdate,
		Boolean solution,
		Boolean deleted,
		String user,
		String topic) {
	
	public AnswerDetailsDTO(Answer answer) {
		this(answer.getId(),
				answer.getBody(),
				answer.getCreationDate(),
				answer.getLastUpdated(),
				answer.getSolution(),
				answer.getDeleted(),
				answer.getUser().getUsername(),
				answer.getTopic().getTitle());
	}

}