package com.alura.foro.api.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{

	Page<Topic> findAll(Pageable pagination);
	
	Page<Topic> findAllByStatusIsNot(Status status, Pageable pagination);
	
	Boolean existsByTitleAndBody(String title, String body);
	
	Topic findByTitle(String title);
	
}