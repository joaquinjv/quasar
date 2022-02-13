package com.ml.quasar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8246532787641646817L;

	public AppException(String message){
		super(message);
	}
	
	public ResponseEntity<?> getRespose(){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
}
