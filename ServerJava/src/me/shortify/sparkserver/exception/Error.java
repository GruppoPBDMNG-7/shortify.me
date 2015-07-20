package me.shortify.sparkserver.exception;

import org.json.JSONObject;

public class Error {
	
	public static final String URL_EXISTS = "The custom text specified alredy exists in the database. Try with another one.";
	public static final String BAD_URL = "The URL specified refers to not safe web site. Try with another URL.";
	public static final String BAD_CUSTOM_URL = "The custom text specified is not accepted.";
	
	private final String ERROR = "error";
	
	private String error;
	
	public Error(String error) {
		this.error = error;
	}
	
	public String toJsonString() {
		JSONObject jo = new JSONObject();	
		jo.put(ERROR, error);
		return jo.toString();
	}
	
}
