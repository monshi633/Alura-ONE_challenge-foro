package com.alura.foro.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.alura.foro.api.domain.user.CreateUserDTO;
import com.alura.foro.api.domain.user.UpdateUserDTO;
import com.alura.foro.api.domain.user.User;
import com.alura.foro.api.domain.user.UserDetailsDTO;
import com.alura.foro.api.domain.user.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository repository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping
	@Transactional
	public ResponseEntity<UserDetailsDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO, UriComponentsBuilder uriBuilder) {
		
		String hashedPassword = passwordEncoder.encode(createUserDTO.password());
		
		var user = new User(createUserDTO, hashedPassword);
		repository.save(user);
		
		var uri = uriBuilder.path("/users/{username}").buildAndExpand(user.getUsername()).toUri();
		return ResponseEntity.created(uri).body(new UserDetailsDTO(user));
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDetailsDTO>> readActiveUsers(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		var page = repository.findAllByEnabledTrue(pagination).map(UserDetailsDTO::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<UserDetailsDTO>> readAllUsers(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		var page = repository.findAll(pagination).map(UserDetailsDTO::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserDetailsDTO> readSingleUser(@PathVariable String username) {
		User user = (User) repository.findByUsername(username);
		var userData = new UserDetailsDTO(user.getId(),
				user.getUsername(),
				user.getRole(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getEnabled()
				);
		return ResponseEntity.ok(userData);
	}
	
	@PutMapping("/{username}")
	@Transactional
	public ResponseEntity<UserDetailsDTO> updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable String username) {
		User user = (User) repository.findByUsername(username);
		if (updateUserDTO.password() != null) {
			String hashedPassword = passwordEncoder.encode(updateUserDTO.password());			
			user.updateUserWithPassword(updateUserDTO, hashedPassword);
		} else {
			user.updateUser(updateUserDTO);
		}
		var userData = new UserDetailsDTO(user.getId(),
				user.getUsername(),
				user.getRole(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getEnabled()
				);
		return ResponseEntity.ok(userData);
	}
	
	@DeleteMapping("/{username}")
	@Transactional
	public ResponseEntity<?> deleteUser(@PathVariable String username) {
		User user = (User) repository.findByUsername(username);
		user.deleteUser();
		return ResponseEntity.noContent().build();
	}
	
}