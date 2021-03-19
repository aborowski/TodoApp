package com.borowski.controllers;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.borowski.repositories.MessageRepository;

@RestController
@RequestMapping(path = "/web-api/messages")
public class MessageRestRepository {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	MessageRepository repository;
	
	@GetMapping
	public List<Message> getMessages() {
		Session session = em.unwrap(Session.class);
		session.enableFilter("filterNotDeletedMessage");
		
		return repository.findAll();
	}
	
	@GetMapping(path = "{id}")
	public Message getMessage(@PathVariable int id) {
		//TODO: filter not deleted somehow. Using filter doesn't work for find one
		return repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id));
	}
	
	@PostMapping
	public Message addMessage(@RequestBody Message message) {
		return repository.save(message);
	}
	
	@PutMapping(path = "{id}")
	public Message updateMessage(@PathVariable int id, @RequestBody Message message) {
		Optional<Message> foundMessage = repository.findById(id);
		if(foundMessage.isPresent())
		{
			foundMessage.get().updateFields(message, true);
			return repository.save(foundMessage.get());
		} else {
			message.setId(id);
			return repository.save(message);
		}
	}

	@PatchMapping(path = "{id}")
	public Message updateMessagePartial(@PathVariable int id, @RequestBody Message message) {
		Message foundMessage = repository.findById(id).orElseThrow(() -> new NoMessageFoundException(id));
		foundMessage.updateFields(message);
		return repository.save(foundMessage);
	}
	
	@DeleteMapping(path = "{id}")
	public void deleteMessage(@PathVariable int id) {
		repository.deleteById(id);
	}
}
