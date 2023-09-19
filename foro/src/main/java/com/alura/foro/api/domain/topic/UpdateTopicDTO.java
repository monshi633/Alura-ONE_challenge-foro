package com.alura.foro.api.domain.topic;

public record UpdateTopicDTO(
		String title,
		String body,
		Status status,
		String course) {
}