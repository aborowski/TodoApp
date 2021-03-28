package com.borowski.controllers.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
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

import com.borowski.exceptions.NoMessageFoundException;
import com.borowski.models.Message;
import com.borowski.models.hateoas.MessageModelAssembler;
import com.borowski.repositories.MessageRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(protocols = "https", tags = "Messages")
@Transactional
@RestController
@RequestMapping(path = "/web-api/messages")
public class MessageRestController {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	MessageRepository repository;
	
	@Autowired
	MessageModelAssembler modelAssembler;
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Messages")
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CollectionModel<EntityModel<Message>>> getMessages() {
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("filterNotDeletedMessage");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(repository.findAll()));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Message by ID")
	@GetMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> getMessageById(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return ResponseEntity.ok(modelAssembler.toModel(repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id))));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Create Message")
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> addMessage(@RequestBody @Valid Message message) {
		EntityModel<Message> entityModel = modelAssembler.toModel(repository.save(message));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Replace Message")
	@PutMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
	@ApiOperation(consumes = "application/json, application/xml", value = "Update Message")
	@PatchMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> updateMessagePartial(@PathVariable int id, @RequestBody Message message) {
		Message foundMessage = repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id));
		foundMessage.updateFields(message);
		return ResponseEntity.ok(modelAssembler.toModel(repository.save(foundMessage)));
	}
	
	@ApiOperation(value = "Delete Message")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteMessage(@PathVariable int id) {
		try {
			repository.deleteById(id);
		return ResponseEntity.noContent().build();
		} catch (EmptyResultDataAccessException ex) {
			throw new NoMessageFoundException(id);
		}
	}
}
