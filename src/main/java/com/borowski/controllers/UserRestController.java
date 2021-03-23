package com.borowski.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

import com.borowski.exceptions.DuplicateUsernameException;
import com.borowski.exceptions.NoUserFoundException;
import com.borowski.models.User;
import com.borowski.models.hateoas.UserModelAssembler;
import com.borowski.repositories.UserRepository;

@Transactional
@RestController
@RequestMapping("/web-api/users")
public class UserRestController {
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserModelAssembler modelAssembler;

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers() {
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("filterUserNotDeleted");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(repository.findAll()));
	}
	
	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<User>> getUserById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return ResponseEntity.ok(modelAssembler.toModel(repository.findById(id).orElseThrow(() -> new NoUserFoundException(id))));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<User>> addeUser(@RequestBody @Valid User user) {
		if(repository.findByUsername(user.getUsername()).isPresent())
			throw new DuplicateUsernameException(user.getUsername());
		
		EntityModel<User> entityModel = modelAssembler.toModel(repository.save(user));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping(path = "{id}")
	public ResponseEntity<EntityModel<User>> updateUser(@PathVariable int id, @RequestBody @Valid User user) {
		EntityModel<User> entityModel = repository.findById(id).map((foundUser) -> {
			foundUser.updateFields(user, true);
			return modelAssembler.toModel(repository.save(foundUser));
		}).orElseGet(() -> {
			user.setId(id);
			return modelAssembler.toModel(repository.save(user));
		});
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	//TODO how to partially validate?
	@PatchMapping(path = "{id}")
	public ResponseEntity<EntityModel<User>> updateUserPartial(@PathVariable int id, @RequestBody User user) {
		User foundUser = repository.findById(id).orElseThrow(() -> new NoUserFoundException(id));
		foundUser.updateFields(user);
		return ResponseEntity.ok(modelAssembler.toModel(repository.save(foundUser)));
	}
	
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		try {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch(EmptyResultDataAccessException ex) {
			throw new NoUserFoundException(id);
		}
	}
}
