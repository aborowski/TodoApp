package com.borowski.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.borowski.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value = "SELECT u FROM #{#entityName} u WHERE u.username = :username")
	public Optional<User> findByUsername(String username);
}
