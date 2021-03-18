package com.borowski.exceptions;

public class NoTaskFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8219353282383191231L;
	
	public NoTaskFoundException(String message) {
		super(message);
	}
}
