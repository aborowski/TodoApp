package com.borowski.repositories;

import org.springframework.data.repository.CrudRepository;

import com.borowski.models.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
