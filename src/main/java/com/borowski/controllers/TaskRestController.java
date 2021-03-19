package com.borowski.controllers;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		session.enableFilter("filterTaskNotDeleted");
		
		return repository.findAll();
	}
	
	@GetMapping(path = "{id}")
	public Task getTaskById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
	}
	
	@PostMapping
	public Task addTask(@RequestBody Task task) {
		return repository.save(task);
	}
	
	@PutMapping(path = "{id}")
	public Task updateTask(@PathVariable int id, @RequestBody Task task) {
		Optional<Task> foundTask = repository.findById(id);
		if(foundTask.isPresent())
		{
			foundTask.get().updateFields(task, true);
			return repository.save(foundTask.get());
		} else {
			task.setId(id);
			return repository.save(task);
		}
	}
	
	@PatchMapping(path = "{id}")
	public Task updateTaskPartial(@PathVariable int id, @RequestBody Task task) {
		Task foundTask = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		foundTask.updateFields(task);
		return repository.save(foundTask);
	}
	
	@DeleteMapping("{id}")
	public void deleteTask(@PathVariable int id) {
		repository.deleteById(id);
	}
}
