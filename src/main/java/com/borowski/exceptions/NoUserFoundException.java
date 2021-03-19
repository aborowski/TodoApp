package com.borowski.exceptions;

public class NoUserFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5553748594557512688L;

	public NoUserFoundException(int id) {
		super("No User found for id: " + id);
	}
}
