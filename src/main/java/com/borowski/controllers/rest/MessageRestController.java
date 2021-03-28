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

import com.borowski.models.Message;
import com.borowski.models.hateoas.MessageModelAssembler;
import com.borowski.services.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(protocols = "https", tags = "Messages")
@RestController
@RequestMapping(path = "/web-api/messages")
public class MessageRestController {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	MessageModelAssembler modelAssembler;
	
	@Autowired
	MessageService messageService;
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Messages")
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CollectionModel<EntityModel<Message>>> getMessages() {
		Session session = entityManager.unwrap(Session.class);
		session.enableFilter("filterNotDeletedMessage");
		
		return ResponseEntity.ok(modelAssembler.toCollectionModel(messageService.getMessages()));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Get Message by ID")
	@GetMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> getMessageById(@PathVariable int id) {
		return ResponseEntity.ok(modelAssembler.toModel(messageService.getMessageById(id)));
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Create Message")
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> addMessage(@RequestBody @Valid Message message) {
		EntityModel<Message> entityModel = modelAssembler.toModel(messageService.createMessage(message));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@ApiOperation(consumes = "application/json, application/xml", value = "Replace Message")
	@PutMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> updateMessage(@PathVariable int id, @RequestBody @Valid Message message) {
		message.setId(id);
		EntityModel<Message> entityModel = modelAssembler.toModel(messageService.replaceMessage(message));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	//TODO how to partially validate?
	@ApiOperation(consumes = "application/json, application/xml", value = "Update Message")
	@PatchMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<EntityModel<Message>> updateMessagePartial(@PathVariable int id, @RequestBody Message message) {
		message.setId(id);
		return ResponseEntity.ok(modelAssembler.toModel(messageService.updateMessage(message)));
	}
	
	@ApiOperation(value = "Delete Message")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteMessage(@PathVariable int id) {
		messageService.deleteMessageById(id);
		return ResponseEntity.noContent().build();
		
	}
}
