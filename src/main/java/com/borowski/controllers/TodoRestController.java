package com.borowski.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.borowski.models.Todo;
import com.borowski.repositories.TodoRepository;

@RestController
@RequestMapping("/web-api/todos")
public class TodoRestController {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	TodoRepository repository;
	
	@GetMapping
	public List<Todo> getTodos() {
		Session session = em.unwrap(Session.class);
		
		session.enableFilter("filterTodoDeleted");
		return (List<Todo>) repository.findAll();
	}
}
