package com.alura.foro.api.domain.course;

public record CourseDetailsDTO(Long id, String name, Category category, Boolean active) {
	public CourseDetailsDTO(Course course) {
		this(course.getId(), course.getName(), course.getCategory(), course.getActive());
	}
}