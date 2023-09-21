package com.alura.foro.api.domain.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseDTO(@NotBlank String name, @NotNull Category category) {
}