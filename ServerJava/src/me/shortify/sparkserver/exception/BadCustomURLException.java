package me.shortify.sparkserver.exception;

public class BadCustomURLException extends RuntimeException {
	
	public BadCustomURLException() {
		
	}
	
	public BadCustomURLException(String err) {
		super(err);
	}
}
