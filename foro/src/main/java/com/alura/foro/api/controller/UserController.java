package com.alura.foro.api.controller;

import java.util.List;

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
import com.alura.foro.api.domain.user.validations.create.CreateUserValidators;
import com.alura.foro.api.domain.user.validations.update.UpdateUserValidators;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "User", description = "Creates topics and posts answers")
public class UserController {

	@Autowired
	private UserRepository repository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	List<CreateUserValidators> createValidators;
	
	@Autowired
	List<UpdateUserValidators> updateValidators;
	
	@PostMapping
	@Transactional
	@Operation(summary = "Registers a new user into the database")
	public ResponseEntity<UserDetailsDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO, UriComponentsBuilder uriBuilder) {
		
		createValidators.forEach(v -> v.validate(createUserDTO));		
		
		String hashedPassword = passwordEncoder.encode(createUserDTO.password());
		User user = new User(createUserDTO, hashedPassword);
		
		repository.save(user);
		
		var uri = uriBuilder.path("/users/{username}").buildAndExpand(user.getUsername()).toUri();
		
		return ResponseEntity.created(uri).body(new UserDetailsDTO(user));
	}
	
	@GetMapping("/all")
	@Operation(summary = "Lists all users regardles of their status")
	public ResponseEntity<Page<UserDetailsDTO>> readAllUsers(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		
		var page = repository.findAll(pagination).map(UserDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping
	@Operation(summary = "Lists only users that are enabled")
	public ResponseEntity<Page<UserDetailsDTO>> readActiveUsers(@PageableDefault(size = 5, sort = {"id"}) Pageable pagination) {
		
		var page = repository.findAllByEnabledTrue(pagination).map(UserDetailsDTO::new);
		
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/username/{username}")
	@Operation(summary = "Reads a single user by its username")
	public ResponseEntity<UserDetailsDTO> readSingleUser(@PathVariable String username) {
		
		User user = (User) repository.findByUsername(username);
		
		var userData = new UserDetailsDTO(
				user.getId(),
				user.getUsername(),
				user.getRole(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getEnabled()
				);
		
		return ResponseEntity.ok(userData);
	}
	
	@GetMapping("/id/{id}")
	@Operation(summary = "Reads a single user by its ID")
	public ResponseEntity<UserDetailsDTO> readSingleUser(@PathVariable Long id) {
		
		User user = repository.getReferenceById(id);
		
		var userData = new UserDetailsDTO(
				user.getId(),
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
	@Operation(summary = "Updates a user password, role, first and last name, email or enabled status")
	public ResponseEntity<UserDetailsDTO> updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO, @PathVariable String username) {
		
		updateValidators.forEach(v -> v.validate(updateUserDTO));	
		
		User user = (User) repository.findByUsername(username);
		
		if (updateUserDTO.password() != null) {
			String hashedPassword = passwordEncoder.encode(updateUserDTO.password());			
			user.updateUserWithPassword(updateUserDTO, hashedPassword);
		} else {
			user.updateUser(updateUserDTO);
		}
		
		var userData = new UserDetailsDTO(
				user.getId(),
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
	@Operation(summary = "Disables a user")
	public ResponseEntity<?> deleteUser(@PathVariable String username) {
		
		User user = (User) repository.findByUsername(username);
		
		user.deleteUser();
		
		return ResponseEntity.noContent().build();
	}
	
}