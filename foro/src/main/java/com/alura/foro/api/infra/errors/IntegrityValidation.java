package com.alura.foro.api.infra.errors;

public class IntegrityValidation extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public IntegrityValidation(String s) {
		super(s);
	}
	
}