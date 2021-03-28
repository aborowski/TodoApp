package com.borowski.controllers.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

import com.borowski.models.Task;
import com.borowski.models.hateoas.TaskModelAssembler;
import com.borowski.services.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Tasks", protocols = "https")
@RestController
@RequestMapping("/web-api/tasks")
public class TaskRestController {
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	TaskModelAssembler modelAssembler;
	
	@Autowired
	TaskService taskService;
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Tasks")
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CollectionModel<EntityModel<Task>>> getTasks() {
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("filterTaskNotDeleted");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(taskService.getTasks()));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Task by ID")
	@GetMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Task>> getTaskById(@PathVariable int id) {
		return ResponseEntity.ok(modelAssembler.toModel(taskService.getTaskById(id)));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Create Task")
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Task>> addTask(@RequestBody @Valid Task task) {
		EntityModel<Task> entityModel = modelAssembler.toModel(taskService.createTask(task));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Replace Task")
	@PutMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Task>> updateTask(@PathVariable int id, @RequestBody @Valid Task task) {
		task.setId(id);
		EntityModel<Task> entityModel = modelAssembler.toModel(taskService.replaceTask(task));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	//TODO how to partially validate?
	@ApiOperation(consumes = "application/json, application/xml", value = "Update Task")
	@PatchMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Task>> updateTaskPartial(@PathVariable int id, @RequestBody Task task) {
		task.setId(id);
		EntityModel<Task> entityModel = modelAssembler.toModel(taskService.updateTask(task));
		return ResponseEntity.ok(entityModel);
	}
	
	@ApiOperation(value = "Delete Task")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteTask(@PathVariable int id) {
		taskService.deleteTaskById(id);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Lower Priority of Task")
	@GetMapping(path = "{id}/lower-priority", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> lowerPriority(@PathVariable Integer id) {
		return ResponseEntity.ok(modelAssembler.toModel(taskService.lowerPriority(id)));
	}

	@ApiOperation(value = "Raise Priority of Task")
	@GetMapping(path = "{id}/raise-priority", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> raisePriority(@PathVariable Integer id) {
		return ResponseEntity.ok(modelAssembler.toModel(taskService.raisePriority(id)));
	}
}
