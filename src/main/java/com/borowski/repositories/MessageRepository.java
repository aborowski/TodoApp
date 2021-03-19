package com.borowski.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borowski.models.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	//TODO: add methods to search all Messages by userId, search all Messages belonging to a Task
}
