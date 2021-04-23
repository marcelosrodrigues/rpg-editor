package com.pmrodrigues.rpg.advices;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pmrodrigues.rpg.exceptions.DuplicatedKeyException;

@ControllerAdvice
public class DuplicateKeyAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler({DuplicatedKeyException.class})
	public ResponseEntity<Object> duplicateKeyException(
		      Exception ex, WebRequest request) {
		
        DuplicatedKeyException dex = (DuplicatedKeyException) ex;
        
        return new ResponseEntity<Object>(dex.getMessage(), new HttpHeaders(),
          HttpStatus.CONFLICT);
		
	}
}
