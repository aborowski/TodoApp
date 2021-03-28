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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.borowski.models.User;
import com.borowski.models.hateoas.UserModelAssembler;
import com.borowski.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Users", protocols = "https")
@RestController
@RequestMapping("/web-api/users")
public class UserRestController {
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	UserModelAssembler modelAssembler;
	
	@Autowired
	UserService userService;

	@ApiOperation(consumes = "application/json, application/xml", value = "Get Users")
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers() {
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("filterUserNotDeleted");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(userService.getUsers()));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get User by ID")
	@GetMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<User>> getUserById(@PathVariable int id) {
		return ResponseEntity.ok(modelAssembler.toModel(userService.getUserById(id)));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Create User")
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<User>> addeUser(@RequestBody @Valid User user) {
		EntityModel<User> entityModel = modelAssembler.toModel(userService.createUser(user));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Replace User")
	@PutMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<User>> updateUser(@PathVariable int id, @RequestBody @Valid User user) {
		user.setId(id);
		EntityModel<User> entityModel = modelAssembler.toModel(userService.replaceUser(user));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	//TODO how to partially validate?
	@ApiOperation(consumes = "application/json, application/xml", value = "Update User")
	@PatchMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<User>> updateUserPartial(@PathVariable int id, @RequestBody User user) {
		user.setId(id);
		return ResponseEntity.ok(modelAssembler.toModel(userService.updateUser(user)));
	}
	
	@ApiOperation(value = "Delete User")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		userService.deleteUserById(id);
		return ResponseEntity.noContent().build();
	}
}
