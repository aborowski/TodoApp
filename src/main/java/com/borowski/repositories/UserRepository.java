package com.borowski.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borowski.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
