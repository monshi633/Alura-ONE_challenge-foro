package com.alura.foro.api.domain.course;

public record UpdateCourseDTO(String name, Category category, Boolean active) {
}