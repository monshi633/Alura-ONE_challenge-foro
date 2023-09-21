package com.alura.foro.api.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTopicDTO(
		@NotBlank String title,
		@NotBlank String body,
		@NotNull Long userId,
		@NotNull Long courseId) {
}