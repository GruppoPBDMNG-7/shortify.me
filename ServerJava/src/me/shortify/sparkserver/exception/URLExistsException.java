package me.shortify.sparkserver.exception;

public class URLExistsException extends RuntimeException {	
	
	public URLExistsException() {
		
	}
	
	public URLExistsException(String err) {
		super(err);
	}
}
