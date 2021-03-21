package com.borowski.exceptions;

public class NoMessageFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1343719239398177231L;

	public NoMessageFoundException(int id) {
		super("No Message found for id: " + id);
	}
}
