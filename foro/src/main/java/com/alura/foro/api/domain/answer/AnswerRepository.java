package com.alura.foro.api.domain.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
	
	Page<Answer> findAllByTopicId(Long topicId, Pageable pagination);
	
	Page<Answer> findAllByUserId(Long userId, Pageable pagination);

	Answer getReferenceByTopicId(Long id);

}