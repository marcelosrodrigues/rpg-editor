package com.pmrodrigues.rpg.exceptions;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

public class NotPermittedException extends ConstraintViolationException {
    public NotPermittedException(String message) {
        super(message, Collections.emptySet());
    }
}
