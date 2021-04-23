package com.pmrodrigues.rpg.exceptions;

import java.util.Collections;

import javax.validation.ConstraintViolationException;

public class DuplicatedKeyException extends ConstraintViolationException {

	private static final long serialVersionUID = 1L;

	public DuplicatedKeyException(String message) {
		super(message, Collections.emptySet());
	}

}
