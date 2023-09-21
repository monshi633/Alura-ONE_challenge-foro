package com.alura.foro.api.domain.course;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
@Entity(name = "Course")
@EqualsAndHashCode(of = "id")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Enumerated(EnumType.STRING)
	private Category category;
	private Boolean active;
	
	public Course(CreateCourseDTO createCourseDTO) {
		this.name = createCourseDTO.name();
		this.category = createCourseDTO.category();
		this.active = true;
	}
	
	public void updateCourse(UpdateCourseDTO updateCourseDTO) {
		if (updateCourseDTO.name() != null) {
			this.name = updateCourseDTO.name();
		}
		if (updateCourseDTO.category() != null) {
			this.category = updateCourseDTO.category();
		}
		if (updateCourseDTO.active() != null) {
			this.active = updateCourseDTO.active();
		}
	}
	
	public void deleteCourse() {
		this.active = false;
	}
}
