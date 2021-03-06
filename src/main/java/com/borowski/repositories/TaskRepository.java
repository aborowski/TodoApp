package com.borowski.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borowski.models.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
