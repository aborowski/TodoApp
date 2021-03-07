package com.borowski.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.borowski.models.Priority;
import com.borowski.models.Todo;

@RestController()
@RequestMapping(path = "todos")
public class TodoRestController {
	
	@RequestMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public List<Todo> getTodos() {
		List<Todo> list = new LinkedList<>();
		
		//TODO: replace with repository
		
		Todo t1 = new Todo("Create controller", "Create REST controller for Todos, CRUD", Priority.CRITICAL);
		Todo t2 = new Todo("Test REST controller", "Test REST controller using Postman", Priority.HIGH);
		
		list.add(t1);
		list.add(t2);
		
		return list;
	}
}
