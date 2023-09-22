package com.alura.foro.api.domain.topic;

import java.time.LocalDateTime;

import com.alura.foro.api.domain.course.Category;

public record TopicDetailsDTO (
		Long id,
		String title,
		String body,
		LocalDateTime creationDate,
		LocalDateTime lastUpdate,
		Status status,
		String user,
		String course,
		Category courseCategory) {
	
	public TopicDetailsDTO(Topic topic) {
		this(topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getUser().getUsername(),
				topic.getCourse().getName(),
				topic.getCourse().getCategory());
	}

}