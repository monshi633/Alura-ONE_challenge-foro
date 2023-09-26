package com.alura.foro.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alura.foro.api.domain.course.Course;
import com.alura.foro.api.domain.course.CourseDetailsDTO;
import com.alura.foro.api.domain.course.CourseRepository;
import com.alura.foro.api.domain.course.CreateCourseDTO;
import com.alura.foro.api.domain.course.UpdateCourseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Course", description = "Can belong to one of many defined categories")
public class CourseController {

	@Autowired
	private CourseRepository repository;
	
	@PostMapping
	@Transactional
	@Operation(summary = "Registers a new course into the database")
	public ResponseEntity<CourseDetailsDTO> createTopic(@RequestBody @Valid CreateCourseDTO createCourseDTO, UriComponentsBuilder uriBuilder) {
		
		Course course = new Course(createCourseDTO);
		
		repository.save(course);
		
		var uri = uriBuilder.path("/courses/{id}").buildAndExpand(course.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new CourseDetailsDTO(course));
	}
	
	@GetMapping("/all")
	@Operation(summary = "Reads all courses regardless of their status")
	public ResponseEntity<Page<CourseDetailsDTO>> readAllCourses(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		
		var page = repository.findAll(pagination).map(CourseDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping
	@Operation(summary = "Lists active courses")
	public ResponseEntity<Page<CourseDetailsDTO>> readActiveCourses(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		
		var page = repository.findAllByActiveTrue(pagination).map(CourseDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Reads a single course by its ID")
	public ResponseEntity<CourseDetailsDTO> readSingleCourse(@PathVariable Long id) {
		
		Course course = repository.getReferenceById(id);
		
		var courseData = new CourseDetailsDTO(
				course.getId(),
				course.getName(),
				course.getCategory(),
				course.getActive()
				);
		
		return ResponseEntity.ok(courseData);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Updates a course name, category or status")
	public ResponseEntity<CourseDetailsDTO> updateCourse(@RequestBody @Valid UpdateCourseDTO updateCourseDTO, @PathVariable Long id) {
		
		Course course = repository.getReferenceById(id);
		
		course.updateCourse(updateCourseDTO);
		
		var courseData = new CourseDetailsDTO(
				course.getId(),
				course.getName(),
				course.getCategory(),
				course.getActive()
				);
		
		return ResponseEntity.ok(courseData);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Deletes a course")
	public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
		
		Course course = repository.getReferenceById(id);
		
		course.deleteCourse();
		
		return ResponseEntity.noContent().build();
	}
	
}
