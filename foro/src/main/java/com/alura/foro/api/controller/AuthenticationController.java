package com.alura.foro.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.foro.api.domain.user.User;
import com.alura.foro.api.domain.user.UserAuthenticationDTO;
import com.alura.foro.api.infra.security.JWTtokenDTO;
import com.alura.foro.api.infra.security.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Gets the designated user's token for endpoint access")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<JWTtokenDTO> authenticateUser(@RequestBody @Valid UserAuthenticationDTO userAuthentication) {
		
		Authentication authToken = new UsernamePasswordAuthenticationToken(userAuthentication.username(),
				userAuthentication.password());
		
		var authentifiedUser = authenticationManager.authenticate(authToken);
		
		var JWTtoken = tokenService.generateToken((User) authentifiedUser.getPrincipal());
		
		return ResponseEntity.ok(new JWTtokenDTO(JWTtoken));
	}

}