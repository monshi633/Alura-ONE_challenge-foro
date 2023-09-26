package com.alura.foro.api.domain.answer.validations.update;

import com.alura.foro.api.domain.answer.UpdateAnswerDTO;

public interface UpdateAnswerValidators {

	public void validate(UpdateAnswerDTO data, Long answerId);

}