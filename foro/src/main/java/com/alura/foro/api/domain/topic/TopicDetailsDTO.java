package com.alura.foro.api.domain.topic;

import java.time.LocalDateTime;

public record TopicDetailsDTO(
		Long id,
		String title,
		String body,
		LocalDateTime creationDate,
		LocalDateTime lastUpdate,
		Status status,
		String user,
		String course) {
	
	public TopicDetailsDTO(Topic topic) {
		this(topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getUser().getUsername(),
				topic.getCourse());
	}

}
