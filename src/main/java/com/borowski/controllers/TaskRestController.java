package com.borowski.controllers;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import com.borowski.models.hateoas.TaskModelAssembler;
import com.borowski.repositories.TaskRepository;

@RestController
@RequestMapping("/web-api/tasks")
public class TaskRestController {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	TaskRepository repository;
	
	@Autowired
	TaskModelAssembler modelAssembler;
	
	@GetMapping
	public CollectionModel<EntityModel<Task>> getTasks() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterTaskNotDeleted");
		
		return modelAssembler.toCollectionModel(repository.findAll());
	}
	
	@GetMapping(path = "{id}")
	public EntityModel<Task> getTaskById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		Task task = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		
		return modelAssembler.toModel(task);
	}
	
	@PostMapping
	public EntityModel<Task> addTask(@RequestBody Task task) {
		Task savedTask = repository.save(task);
		
		return modelAssembler.toModel(savedTask);
	}
	
	@PutMapping(path = "{id}")
	public EntityModel<Task> updateTask(@PathVariable int id, @RequestBody Task task) {
		Optional<Task> foundTask = repository.findById(id);
		if(foundTask.isPresent())
		{
			foundTask.get().updateFields(task, true);
			return modelAssembler.toModel(repository.save(foundTask.get()));
		} else {
			task.setId(id);
			return modelAssembler.toModel(repository.save(task));
		}
	}
	
	@PatchMapping(path = "{id}")
	public EntityModel<Task> updateTaskPartial(@PathVariable int id, @RequestBody Task task) {
		Task foundTask = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		foundTask.updateFields(task);
		Task updatedTask = repository.save(foundTask);
		
		return modelAssembler.toModel(updatedTask);
	}
	
	@DeleteMapping("{id}")
	public void deleteTask(@PathVariable int id) {
		repository.deleteById(id);
	}
}
