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

import com.borowski.exceptions.NoUserFoundException;
import com.borowski.models.User;
import com.borowski.models.hateoas.UserModelAssembler;
import com.borowski.repositories.UserRepository;

@RestController
@RequestMapping("/web-api/users")
public class UserRestController {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserModelAssembler modelAssembler;

	@GetMapping
	public CollectionModel<EntityModel<User>> getUsers() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterUserNotDeleted");
		
		return modelAssembler.toCollectionModel(repository.findAll());
	}
	
	@GetMapping(path = "{id}")
	public EntityModel<User> getUserById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return modelAssembler.toModel(repository.findById(id).orElseThrow(() -> new NoUserFoundException(id)));
	}
	
	@PostMapping
	public EntityModel<User> addeUser(@RequestBody User user) {
		return modelAssembler.toModel(repository.save(user));
	}
	
	@PutMapping(path = "{id}")
	public EntityModel<User> updateUser(@PathVariable int id, @RequestBody User user) {
		Optional<User> foundUser = repository.findById(id);
		if(foundUser.isPresent())
		{
			foundUser.get().updateFields(user, true);
			return modelAssembler.toModel(repository.save(foundUser.get()));
		} else {
			user.setId(id);
			return modelAssembler.toModel(repository.save(user));
		}
	}
	
	@PatchMapping(path = "{id}")
	public EntityModel<User> updateUserPartial(@PathVariable int id, @RequestBody User user) {
		User foundUser = repository.findById(id).orElseThrow(() -> new NoUserFoundException(id));
		foundUser.updateFields(user);
		return modelAssembler.toModel(repository.save(foundUser));
	}
	
	@DeleteMapping(path = "{id}")
	public void deleteUser(@PathVariable int id) {
		repository.deleteById(id);
	}
}
