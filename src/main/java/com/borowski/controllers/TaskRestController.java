package com.borowski.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.borowski.models.Priority;
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
	public ResponseEntity<CollectionModel<EntityModel<Task>>> getTasks() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterTaskNotDeleted");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(repository.findAll()));
	}
	
	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<Task>> getTaskById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		Task task = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		
		return ResponseEntity.ok(modelAssembler.toModel(task));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Task>> addTask(@RequestBody @Valid Task task) {
		Task savedTask = repository.save(task);
		
		EntityModel<Task> entityModel = modelAssembler.toModel(savedTask);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping(path = "{id}")
	public ResponseEntity<EntityModel<Task>> updateTask(@PathVariable int id, @RequestBody @Valid Task task) {
		Task postTask = repository.findById(id).map((foundTask) -> {
			return repository.save(foundTask);
		}).orElseGet(() -> {
			task.setId(id);
			return repository.save(task);
		});
		
		EntityModel<Task> entityModel = modelAssembler.toModel(postTask);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	//TODO how to partially validate?
	@PatchMapping(path = "{id}")
	public ResponseEntity<EntityModel<Task>> updateTaskPartial(@PathVariable int id, @RequestBody Task task) {
		Task foundTask = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		foundTask.updateFields(task);
		EntityModel<Task> entityModel = modelAssembler.toModel(repository.save(foundTask));
		
		return ResponseEntity.ok(entityModel);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteTask(@PathVariable int id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}/lower-priority")
	public ResponseEntity<?> lowerPriority(@PathVariable Integer id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new NoTaskFoundException(id));

		if(task.getPriority() != Priority.LOW) {
			task.setPriority(task.getPriority().previous());
			return ResponseEntity.ok(modelAssembler.toModel(repository.save(task)));
		}
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail("Cannot lower priority for task with " + task.getPriority() + " priority."));
	}

	@GetMapping("{id}/raise-priority")
	public ResponseEntity<?> raisePriority(@PathVariable Integer id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new NoTaskFoundException(id));

		if(task.getPriority() != Priority.CRITICAL) {
			task.setPriority(task.getPriority().next());
			return ResponseEntity.ok(modelAssembler.toModel(repository.save(task)));
		}
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail("Cannot raise priority for task with " + task.getPriority() + " priority."));
	}
}
