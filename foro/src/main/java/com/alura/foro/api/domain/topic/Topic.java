package com.alura.foro.api.domain.topic;

import java.time.LocalDateTime;

import com.alura.foro.api.domain.course.Course;
import com.alura.foro.api.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topics")
@Entity(name = "Topic")
@EqualsAndHashCode(of = "id")
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String body;
	private LocalDateTime creationDate;
	private LocalDateTime lastUpdated;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
	private Course course;

	public Topic(CreateTopicDTO createTopicDTO, User user, Course course) {
		this.title = createTopicDTO.title();
		this.body = createTopicDTO.body();
		this.creationDate = LocalDateTime.now();
		this.lastUpdated = LocalDateTime.now();
		this.status = Status.OPEN;
		this.user = user;
		this.course = course;
	}
	
	public void updateTopicWithCourse(UpdateTopicDTO updateTopicDTO, Course course) {
		if (updateTopicDTO.title() != null) {
			this.title = updateTopicDTO.title();
		}
		if (updateTopicDTO.body() != null) {
			this.body = updateTopicDTO.body();
		}
		if (updateTopicDTO.status() != null) {
			this.status = updateTopicDTO.status();
		}
		if (updateTopicDTO.courseId() != null) {
			this.course = course;
		}
		this.lastUpdated = LocalDateTime.now();
	}
	
	public void updateTopic(UpdateTopicDTO updateTopicDTO) {
		if (updateTopicDTO.title() != null) {
			this.title = updateTopicDTO.title();
		}
		if (updateTopicDTO.body() != null) {
			this.body = updateTopicDTO.body();
		}
		if (updateTopicDTO.status() != null) {
			this.status = updateTopicDTO.status();
		}
		this.lastUpdated = LocalDateTime.now();
	}
	
	public void deleteTopic() {
		this.status = Status.DELETED;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}