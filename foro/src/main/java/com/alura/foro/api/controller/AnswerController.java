package com.alura.foro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

import com.alura.foro.api.domain.answer.Answer;
import com.alura.foro.api.domain.answer.AnswerDetailsDTO;
import com.alura.foro.api.domain.answer.AnswerRepository;
import com.alura.foro.api.domain.answer.CreateAnswerDTO;
import com.alura.foro.api.domain.answer.UpdateAnswerDTO;
import com.alura.foro.api.domain.answer.validations.create.CreateAnswerValidators;
import com.alura.foro.api.domain.answer.validations.update.UpdateAnswerValidators;
import com.alura.foro.api.domain.topic.Status;
import com.alura.foro.api.domain.topic.Topic;
import com.alura.foro.api.domain.topic.TopicRepository;
import com.alura.foro.api.domain.user.User;
import com.alura.foro.api.domain.user.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/answers")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Answer", description = "Only one can be the solution to it's topic")
public class AnswerController {

	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	List<CreateAnswerValidators> createValidators;
	
	@Autowired
	List<UpdateAnswerValidators> updateValidators;
	
	@PostMapping
	@Transactional
	@Operation(summary = "Registers a new answer into the database, linked to an existing user and topic")
	public ResponseEntity<AnswerDetailsDTO> createAnswer(@RequestBody @Valid CreateAnswerDTO createAnswerDTO, UriComponentsBuilder uriBuilder) {
		
		createValidators.forEach(v -> v.validate(createAnswerDTO));
		
		User user = userRepository.getReferenceById(createAnswerDTO.userId());
		Topic topic = topicRepository.getReferenceById(createAnswerDTO.topicId());
		
		var answer = new Answer(createAnswerDTO, user, topic);
		
		answerRepository.save(answer);
		
		var uri = uriBuilder.path("/answers/{id}").buildAndExpand(answer.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new AnswerDetailsDTO(answer));
	}
	
	@GetMapping("/topic/{topicId}")
	@Operation(summary = "Reads all answers of the given topic")
	public ResponseEntity<Page<AnswerDetailsDTO>> readAnswersFromTopic(@PageableDefault(size = 5, sort = {"lastUpdated"}, direction = Direction.ASC) Pageable pagination, @PathVariable Long topicId) {
		
		var page = answerRepository.findAllByTopicId(topicId, pagination).map(AnswerDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/user/{username}")
	@Operation(summary = "Reads all answers from the given username")
	public ResponseEntity<Page<AnswerDetailsDTO>> readAnswersFromUser(@PageableDefault(size = 5, sort = {"lastUpdated"}, direction = Direction.DESC) Pageable pagination, @PathVariable Long userId) {
		
		var page = answerRepository.findAllByUserId(userId, pagination).map(AnswerDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Reads a single answer by its ID")
	public ResponseEntity<AnswerDetailsDTO> readSingleAnswer(@PathVariable Long id) {
		
		Answer answer = answerRepository.getReferenceById(id);
		
		var answerData = new AnswerDetailsDTO(
				answer.getId(),
				answer.getBody(),
				answer.getCreationDate(),
				answer.getLastUpdated(),
				answer.getSolution(),
				answer.getDeleted(),
				answer.getUser().getId(),
				answer.getUser().getUsername(),
				answer.getTopic().getId(),
				answer.getTopic().getTitle()
				);
		
		return ResponseEntity.ok(answerData);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Updates an answer body, solution or status")
	public ResponseEntity<AnswerDetailsDTO> updateAnswer(@RequestBody @Valid UpdateAnswerDTO updateAnswerDTO, @PathVariable Long id) {
		
		updateValidators.forEach(v -> v.validate(updateAnswerDTO, id));
		
		Answer answer = answerRepository.getReferenceById(id);
		
		answer.updateAnswer(updateAnswerDTO);
		
		if (updateAnswerDTO.solution()) {
			var solvedTopic = topicRepository.getReferenceById(answer.getTopic().getId());
			solvedTopic.setStatus(Status.CLOSED);
		}
		
		var answerData = new AnswerDetailsDTO(
				answer.getId(),
				answer.getBody(),
				answer.getCreationDate(),
				answer.getLastUpdated(),
				answer.getSolution(),
				answer.getDeleted(),
				answer.getUser().getId(),
				answer.getUser().getUsername(),
				answer.getTopic().getId(),
				answer.getTopic().getTitle()
				);
		
		return ResponseEntity.ok(answerData);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Deletes an answer")
	public ResponseEntity<?> deleteAnswer(@PathVariable Long id) {
		
		Answer answer = answerRepository.getReferenceById(id);
		
		answer.deleteAnswer();
		
		return ResponseEntity.noContent().build();
	}
	
}