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
import com.alura.foro.api.domain.course.Course;
import com.alura.foro.api.domain.course.CourseRepository;
import com.alura.foro.api.domain.topic.CreateTopicDTO;
import com.alura.foro.api.domain.topic.Status;
import com.alura.foro.api.domain.topic.Topic;
import com.alura.foro.api.domain.topic.TopicDetailsDTO;
import com.alura.foro.api.domain.topic.TopicRepository;
import com.alura.foro.api.domain.topic.UpdateTopicDTO;
import com.alura.foro.api.domain.topic.validations.create.CreateTopicValidators;
import com.alura.foro.api.domain.topic.validations.update.UpdateTopicValidators;
import com.alura.foro.api.domain.user.User;
import com.alura.foro.api.domain.user.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topic", description = "It's linked to a specific course and user")
public class TopicController {

	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	List<CreateTopicValidators> createValidators;

	@Autowired
	List<UpdateTopicValidators> updateValidators;
	
	@PostMapping
	@Transactional
	@Operation(summary = "Registers a new topic into the database")
	public ResponseEntity<TopicDetailsDTO> createTopic(@RequestBody @Valid CreateTopicDTO createTopicDTO, UriComponentsBuilder uriBuilder) {
		
		createValidators.forEach(v -> v.validate(createTopicDTO));
		
		User user = userRepository.findById(createTopicDTO.userId()).get();
		Course course = courseRepository.findById(createTopicDTO.courseId()).get();
		Topic topic = new Topic(createTopicDTO, user, course);
		
		topicRepository.save(topic);
		

		var uri = uriBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicDetailsDTO(topic));
	}
	
	@GetMapping("/all")
	@Operation(summary = "Reads all topics regardless of their status")
	public ResponseEntity<Page<TopicDetailsDTO>> readAllTopics(@PageableDefault(size = 5, sort = {"lastUpdated"}, direction = Direction.DESC) Pageable pagination) {
		
		var page = topicRepository.findAll(pagination).map(TopicDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping
	@Operation(summary = "Lists open and closed topics")
	public ResponseEntity<Page<TopicDetailsDTO>> readNonDeletedTopics(@PageableDefault(size = 5, sort = {"lastUpdated"}, direction = Direction.DESC) Pageable pagination) {
		
		var page = topicRepository.findAllByStatusIsNot(Status.DELETED,pagination).map(TopicDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Reads a single topic by its ID")
	public ResponseEntity<TopicDetailsDTO> readSingleTopic(@PathVariable Long id) {
		
		Topic topic = topicRepository.getReferenceById(id);
		
		var topicData = new TopicDetailsDTO(
				topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getUser().getUsername(),
				topic.getCourse().getName(),
				topic.getCourse().getCategory()
				);
		
		return ResponseEntity.ok(topicData);
	}
	
	@GetMapping("/{id}/solution")
	@Operation(summary = "Reads the topic's answer marked as its solution")
	public ResponseEntity<AnswerDetailsDTO> readTopicSolution(@PathVariable Long id) {
		
		Answer answer = answerRepository.getReferenceByTopicId(id);
		
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
	@Operation(summary = "Updates a topic title, body, status or course ID")
	public ResponseEntity<TopicDetailsDTO> updateTopic(@RequestBody @Valid UpdateTopicDTO updateTopicDTO, @PathVariable Long id) {
		
		updateValidators.forEach(v -> v.validate(updateTopicDTO));
		
		Topic topic = topicRepository.getReferenceById(id);
		
		if (updateTopicDTO.courseId() != null) {
			Course course = courseRepository.getReferenceById(updateTopicDTO.courseId());
			topic.updateTopicWithCourse(updateTopicDTO, course);
		} else {
			topic.updateTopic(updateTopicDTO);
		}
		
		var topicData = new TopicDetailsDTO(
				topic.getId(),
				topic.getTitle(),
				topic.getBody(),
				topic.getCreationDate(),
				topic.getLastUpdated(),
				topic.getStatus(),
				topic.getUser().getUsername(),
				topic.getCourse().getName(),
				topic.getCourse().getCategory()
				);
		
		return ResponseEntity.ok(topicData);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Deletes a topic")
	public ResponseEntity<?> deleteTopic(@PathVariable Long id) {
		
		Topic topic = topicRepository.getReferenceById(id);
		
		topic.deleteTopic();
		
		return ResponseEntity.noContent().build();
	}
	
}