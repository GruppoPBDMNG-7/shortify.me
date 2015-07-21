package me.shortify.sparkserver;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import me.shortify.dao.CassandraDAO;
import me.shortify.sparkserver.exception.BadCustomURLException;
import me.shortify.sparkserver.exception.BadURLException;
import me.shortify.sparkserver.exception.Error;
import me.shortify.sparkserver.exception.ShortURLNotFoundException;
import me.shortify.sparkserver.exception.URLExistsException;



public class Services {
	
	private static final String API_CONTEXT = "/api/v1";

	
	public static void setupEndpoints() {			
		ShortenerServices shortenerServices = new ShortenerServices(new CassandraDAO());
		set404();
		setConversione(shortenerServices);
    	setVisitaShortUrl(shortenerServices); 
    	setIspezioneUrl(shortenerServices);
    	setOpzioni();
    }
	
	
	private static void setConversione(ShortenerServices ss) {
		post(API_CONTEXT + API.CONVERT, (request, response) -> {	
			String result = null;
			
			try {
				result = ss.conversioneURL(request.body());
				
			} catch(URLExistsException e) {
				System.err.println(e.getMessage());
				result = new Error(Error.URL_EXISTS).toJsonString();
				response.status(500);
				
			} catch(BadCustomURLException e) {
				System.err.println(e.getMessage());
				result = new Error(Error.BAD_CUSTOM_URL).toJsonString();
				response.status(500);
				
			} catch(BadURLException e) {
				System.err.println(e.getMessage());
				result = new Error(Error.BAD_URL).toJsonString();
				response.status(400);
			}
			
			return result;
    	});
	}
	
	private static void set404() {
		get("/404.html", (request, response) -> {
			return null;
		});
	}
	
	private static void setVisitaShortUrl(ShortenerServices ss) {
    	get("/:goto", (request, response) -> {
    		
    		try {
    			String longUrl = ss.visitaURL(request.params(":goto"), request.ip());
    			response.redirect(longUrl);
    			
    		} catch(ShortURLNotFoundException e) {
    			System.err.println(e.getMessage());
				response.redirect("404.html");
    		}

    		return null;		
    	});
	}

	private static void setIspezioneUrl(ShortenerServices ss) {
		post(API_CONTEXT + API.STATS, (request, response) -> {
			String result = "";
			
			try {
				result = ss.ispezionaURL(request.body());
				
			} catch (ShortURLNotFoundException e) {
				System.err.println(e.getMessage());		
				result = new Error(Error.SHORT_URL_NOT_EXISTS).toJsonString();
				response.status(400);
			} catch (BadURLException e) {
				System.err.println(e.getMessage());
				result = new Error(Error.URL_NOT_SPECIFIED).toJsonString();
				response.status(400);
			}
			return result;
		});
	}
	
	private static void setOpzioni() {
    	options("/*", (request,response)->{

    	    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    	    if (accessControlRequestHeaders != null) {
    	        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    	    }

    	    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    	    if(accessControlRequestMethod != null){
    		response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    	    }

    	    return "OK";
    	});

    	before((request,response)->{
    	    response.header("Access-Control-Allow-Origin", "*");
    	});
	}
}
