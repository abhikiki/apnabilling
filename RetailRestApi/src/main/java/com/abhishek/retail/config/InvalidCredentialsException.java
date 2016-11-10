package com.abhishek.retail.config;

import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException() {
        super("Username/Password not found. Invalid_credentials");
    }

}