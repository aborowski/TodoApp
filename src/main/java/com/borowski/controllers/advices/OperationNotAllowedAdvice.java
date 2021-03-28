package com.borowski.controllers.advices;

import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.borowski.exceptions.OperationNotAllowedException;

@RestControllerAdvice
public class OperationNotAllowedAdvice {

	@ExceptionHandler(OperationNotAllowedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ResponseEntity<?> handleOperationNotAllowed(OperationNotAllowedException ex) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail(ex.getMessage()));
	}
}
