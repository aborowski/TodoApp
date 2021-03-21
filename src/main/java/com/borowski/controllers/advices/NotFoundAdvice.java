package com.borowski.controllers.advices;

import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.borowski.exceptions.NoMessageFoundException;
import com.borowski.exceptions.NoTaskFoundException;
import com.borowski.exceptions.NoUserFoundException;

@RestControllerAdvice
public class NotFoundAdvice {

	@ExceptionHandler({NoMessageFoundException.class, NoTaskFoundException.class, NoUserFoundException.class})
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	ResponseEntity<?> messageNotFound(Exception ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(Problem.create()
						.withTitle("Resource not found")
						.withDetail(ex.getMessage()));
	}
}
