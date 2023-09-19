package com.alura.foro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alura.foro.api.domain.topic.CreateTopicDTO;
import com.alura.foro.api.domain.topic.Status;
import com.alura.foro.api.domain.topic.Topic;
import com.alura.foro.api.domain.topic.TopicDetailsDTO;
import com.alura.foro.api.domain.topic.TopicRepository;
import com.alura.foro.api.domain.topic.UpdateTopicDTO;
import com.alura.foro.api.domain.topic.validations.TopicValidators;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics")
public class TopicsController {

	@Autowired
	private TopicRepository repository;
	
	@Autowired
	List<TopicValidators> validators;
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicDetailsDTO> createTopic(@RequestBody @Valid CreateTopicDTO createTopicDTO, UriComponentsBuilder uribBuilder) {
		
		validators.forEach(v -> v.validate(createTopicDTO));
		
		var topic = new Topic(createTopicDTO);
		repository.save(topic);
		
		var uri = uribBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicDetailsDTO(topic));
		
	}
	
	@GetMapping
	public ResponseEntity<Page<TopicDetailsDTO>> readNonArchivedTopics(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		var page = repository.findAllByStatusIsNot(Status.ARCHIVED,pagination).map(TopicDetailsDTO::new);
		return ResponseEntity.ok(page);
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<TopicDetailsDTO>> readAllTopics(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		var page = repository.findAll(pagination).map(TopicDetailsDTO::new);
		return ResponseEntity.ok(page);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicDetailsDTO> readSingleTopic(@PathVariable Long id) {
		Topic topic = repository.getReferenceById(id);
		var topicData = new TopicDetailsDTO(topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getAuthor(),
				topic.getCourse()
				);
		return ResponseEntity.ok(topicData);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity updateTopic(@RequestBody @Valid UpdateTopicDTO updateTopicDTO) {
		Topic topic = repository.getReferenceById(updateTopicDTO.id());
		topic.updateTopic(updateTopicDTO);
		var topicData = new TopicDetailsDTO(topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getAuthor(),
				topic.getCourse()
				);
		return ResponseEntity.ok(topicData);
		
	}
	
	
}