package com.borowski.repositories;

import org.springframework.data.repository.CrudRepository;

import com.borowski.models.Todo;

public interface TodoRepository extends CrudRepository<Todo, Integer> {
}
