package com.borowski.exceptions;

public class OperationNotAllowedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 304454300932885570L;

	
	public OperationNotAllowedException(Class<?> objectType, String operation, int id) {
		super("Operation " + operation + " is not allowed on resource " + objectType.getSimpleName() + " with id: " + id);
	}
	
	public OperationNotAllowedException(String message) {
		super(message);
	}
}
