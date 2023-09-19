package com.alura.foro.api.infra.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;

@RestControllerAdvice
public class ErrorsTreatment {

	@ExceptionHandler(IntegrityValidation.class)
	public ResponseEntity<String> errorHandlerIntegrityValidation(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> errorHandlerBussinessValidation(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
}
