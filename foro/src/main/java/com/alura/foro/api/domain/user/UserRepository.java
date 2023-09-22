package com.alura.foro.api.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	UserDetails findByUsername(String username);

	Page<User> findAllByEnabledTrue(Pageable pagination);
	
	User getReferenceByUsername(String username);

}