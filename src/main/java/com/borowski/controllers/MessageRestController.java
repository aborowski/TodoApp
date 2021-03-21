package com.borowski.controllers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
	public ResponseEntity<CollectionModel<EntityModel<Message>>> getMessages() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterNotDeletedMessage");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(repository.findAll()));
	}
	
	@GetMapping(path = "{id}")
	public ResponseEntity<EntityModel<Message>> getMessageById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return ResponseEntity.ok(modelAssembler.toModel(repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id))));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Message>> addMessage(@RequestBody @Valid Message message) {
		EntityModel<Message> entityModel = modelAssembler.toModel(repository.save(message));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping(path = "{id}")
	public ResponseEntity<EntityModel<Message>> updateMessage(@PathVariable int id, @RequestBody @Valid Message message) {
		EntityModel<Message> entityModel = repository.findById(id).map((foundMessage) -> {
			foundMessage.updateFields(message, true);
			return modelAssembler.toModel(repository.save(foundMessage));
		}).orElseGet(() -> {
			message.setId(id);
			return modelAssembler.toModel(repository.save(message));
		});
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	//TODO how to partially validate?
	@PatchMapping(path = "{id}")
	public ResponseEntity<EntityModel<Message>> updateMessagePartial(@PathVariable int id, @RequestBody Message message) {
		Message foundMessage = repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id));
		foundMessage.updateFields(message);
		return ResponseEntity.ok(modelAssembler.toModel(repository.save(foundMessage)));
	}
	
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteMessage(@PathVariable int id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
