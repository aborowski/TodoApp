package com.borowski.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.borowski.exceptions.NoMessageFoundException;
import com.borowski.exceptions.NoTaskFoundException;
import com.borowski.exceptions.NoUserFoundException;

@RestControllerAdvice
public class NotFoundAdvice {

	@ExceptionHandler(NoMessageFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	String messageNotFound(NoMessageFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(NoTaskFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	String taskNotFound(NoTaskFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(NoUserFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	String userNotFound(NoUserFoundException ex) {
		return ex.getMessage();
	}
}
