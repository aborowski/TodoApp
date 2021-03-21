package com.borowski.controllers.advices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.borowski.exceptions.DuplicateUsernameException;
import com.borowski.util.MapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class ValidationAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<Problem> handleMethodArgumentNotValidExceptions(
	  MethodArgumentNotValidException ex) throws JsonProcessingException {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.contentType(MediaType.APPLICATION_PROBLEM_JSON)
			.body(Problem.create()
					.withTitle("Validation failed")
					.withDetail(MapUtils.printMap(errors)));
	}
	
	@ExceptionHandler(DuplicateUsernameException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleValidatioNExceptions(DuplicateUsernameException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(Problem.create()
						.withTitle("Validation failed")
						.withDetail(ex.getMessage()));
	}
	
}
