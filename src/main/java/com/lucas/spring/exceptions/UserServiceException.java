package com.lucas.spring.exceptions;

public class UserServiceException extends RuntimeException{

	private static final long serialVersionUID = 5464649254718498897L;
	
	public UserServiceException(String message) {
		super(message);
	}
}
