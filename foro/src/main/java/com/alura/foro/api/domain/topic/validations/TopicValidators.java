package com.alura.foro.api.domain.topic.validations;

import com.alura.foro.api.domain.topic.CreateTopicDTO;

public interface TopicValidators {
	public void validate(CreateTopicDTO data);
}
