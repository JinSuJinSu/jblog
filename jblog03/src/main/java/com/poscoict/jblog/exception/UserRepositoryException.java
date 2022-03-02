package com.poscoict.jblog.exception;

public class UserRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserRepositoryException(String message) {
		super(message);
	}

	public UserRepositoryException() {
		super("UserRepositoryException Occurs");
	}
}
