package me.shortify.sparkserver.exception;

public class ShortURLNotFoundException extends RuntimeException {
	
	public ShortURLNotFoundException() {
		
	}
	
	public ShortURLNotFoundException(String err) {
		super(err);
	}
}
