package me.shortify.sparkserver.exception;

public class BadURLException extends RuntimeException {
	
	public BadURLException() {
		
	}
	
	public BadURLException(String err) {
		super(err);
	}
}
