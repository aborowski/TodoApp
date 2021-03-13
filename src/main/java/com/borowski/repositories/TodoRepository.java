package com.borowski.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.borowski.models.Todo;

@RepositoryRestResource
public interface TodoRepository extends CrudRepository<Todo, Integer> {
}
