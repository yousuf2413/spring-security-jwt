package com.security.springsecurityjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.springsecurityjwt.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	public Optional<User> findByUserName(String userName);

}
