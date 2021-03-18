package com.borowski.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.borowski.exceptions.NoTaskFoundException;
import com.borowski.models.Task;
import com.borowski.repositories.TaskRepository;

@RestController
@RequestMapping("/web-api/tasks")
public class TaskRestController {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	TaskRepository repository;
	
	@GetMapping
	public List<Task> getTasks() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterTodoNotDeleted");
		
		return (List<Task>) repository.findAll();
	}
	
	@GetMapping(path = "{id}")
	public Task getTaskById(@Param("id") int id) {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterTodoNotDeleted");
		
		return repository.findById(id).orElseThrow(() -> new NoTaskFoundException("No Todo exists with supplied id"));
	}
	
	@PostMapping
	public Task addTask(Task todo) {
		//TODO:
		return null;
	}
	
	@PutMapping
	public Task replaceTask(Task todo) {
		//TODO:
		return null;
	}
	
	@PatchMapping(path = "{id}")
	public Task updateTaskById(@Param("id") int id, Task todo) {
		//TODO:
		return null;
	}
	
	@DeleteMapping("{id}")
	public void deleteTask(@Param("id") int id) {
		repository.deleteById(id);
	}
}
