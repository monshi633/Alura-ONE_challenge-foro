package com.alura.foro.api.domain.topic;

import java.time.LocalDateTime;

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
	@Enumerated(EnumType.STRING)
	private Status status;
	private String author;
	private String course;

	public Topic(CreateTopicDTO createTopicDTO) {
		this.title = createTopicDTO.title();
		this.body = createTopicDTO.body();
		this.creationDate = LocalDateTime.now();
		this.status = Status.OPEN;
		this.author = createTopicDTO.author(); //Falta hacer que se agregue automatico el usuario activo como autor. Se modifica CreateTopicDTO y creo que TopicsController
		this.course = createTopicDTO.course();
	}
}