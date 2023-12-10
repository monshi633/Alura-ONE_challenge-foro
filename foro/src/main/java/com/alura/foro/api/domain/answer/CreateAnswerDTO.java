package com.alura.foro.api.domain.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerDTO(
		@NotBlank String body,
		@NotNull Long userId,
		@NotNull Long topicId
		) {}