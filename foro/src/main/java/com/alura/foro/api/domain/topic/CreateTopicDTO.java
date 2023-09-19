package com.alura.foro.api.domain.topic;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicDTO(
		@NotBlank String title,
		@NotBlank String body,
		@NotBlank String author,
		@NotBlank String course) {
}