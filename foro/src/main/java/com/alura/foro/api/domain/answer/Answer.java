package com.alura.foro.api.domain.answer;

import java.time.LocalDateTime;

import com.alura.foro.api.domain.topic.Topic;
import com.alura.foro.api.domain.user.User;

import jakarta.persistence.Entity;
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
@Table(name = "answers")
@Entity(name = "Answer")
@EqualsAndHashCode(of = "id")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String body;
	private LocalDateTime creationDate;
	private LocalDateTime lastUpdated;
	private Boolean solution;
	private Boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "topic_id")
	private Topic topic;

	public Answer(CreateAnswerDTO createAnswerDTO, User user, Topic topic) {
		this.body = createAnswerDTO.body();
		this.creationDate = LocalDateTime.now();
		this.lastUpdated = LocalDateTime.now();
		this.solution = false;
		this.deleted = false;
		this.user = user;
		this.topic = topic;
	}

	public void updateAnswer(UpdateAnswerDTO updateAnswerDTO) {
		if (updateAnswerDTO.body() != null) {
			this.body = updateAnswerDTO.body();
		}
		if (updateAnswerDTO.solution() != null) {
			this.solution = updateAnswerDTO.solution();
		}
		this.lastUpdated = LocalDateTime.now();
	}
	
	public void deleteAnswer() {
		this.deleted = true;
	}

}