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

import com.borowski.exceptions.NoMessageFoundException;
import com.borowski.models.Message;
import com.borowski.models.hateoas.MessageModelAssembler;
import com.borowski.repositories.MessageRepository;

@RestController
@RequestMapping(path = "/web-api/messages")
public class MessageRestController {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	MessageRepository repository;
	
	@Autowired
	MessageModelAssembler modelAssembler;
	
	@GetMapping
	public CollectionModel<EntityModel<Message>> getMessages() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterNotDeletedMessage");
		
		return modelAssembler.toCollectionModel(repository.findAll());
	}
	
	@GetMapping(path = "{id}")
	public EntityModel<Message> getMessageById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return modelAssembler.toModel(repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id)));
	}
	
	@PostMapping
	public EntityModel<Message> addMessage(@RequestBody Message message) {
		return modelAssembler.toModel(repository.save(message));
	}
	
	@PutMapping(path = "{id}")
	public EntityModel<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {
		Optional<Message> foundMessage = repository.findById(id);
		if(foundMessage.isPresent())
		{
			foundMessage.get().updateFields(message, true);
			return modelAssembler.toModel(repository.save(foundMessage.get()));
		} else {
			message.setId(id);
			return modelAssembler.toModel(repository.save(message));
		}
	}

	@PatchMapping(path = "{id}")
	public EntityModel<Message> updateMessagePartial(@PathVariable int id, @RequestBody Message message) {
		Message foundMessage = repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id));
		foundMessage.updateFields(message);
		return modelAssembler.toModel(repository.save(foundMessage));
	}
	
	@DeleteMapping(path = "{id}")
	public void deleteMessage(@PathVariable int id) {
		repository.deleteById(id);
	}
}
