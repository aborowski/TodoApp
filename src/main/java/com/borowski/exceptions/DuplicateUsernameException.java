package com.borowski.exceptions;

public class DuplicateUsernameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797319243886527724L;

	public DuplicateUsernameException(String username) {
		super("Username " + username + " already in use");
	}
}
