package com.alura.foro.api.infra.errors;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;

@RestControllerAdvice
public class ErrorsTreatment {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<List<?>> error400Handler(MethodArgumentNotValidException e){
		var error = e.getFieldErrors().stream().map(DataErrorsValidation::new).toList();
		return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> error404Handler(){
        return ResponseEntity.notFound().build();
    }
	
	@ExceptionHandler(IntegrityValidation.class)
	public ResponseEntity<String> errorHandlerIntegrityValidation(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> errorHandlerBussinessValidation(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> invalidBodyHandler(Exception e) {
		return ResponseEntity.badRequest().body("Some parts of the request body are wrongly stated");
	}
	
	private record DataErrorsValidation(String field, String error){
        public DataErrorsValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
	
}