package me.shortify.sparkserver;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import me.shortify.dao.CassandraDAO;
import me.shortify.sparkserver.exception.BadCustomURLException;
import me.shortify.sparkserver.exception.BadURLException;
import me.shortify.sparkserver.exception.ShortURLNotFoundException;
import me.shortify.sparkserver.exception.URLExistsException;

public class Services {
	
	private static final String API_CONTEXT = "/api/v1";

	
	public static void setupEndpoints() {			
		ShortenerServices shortenerServices = new ShortenerServices(new CassandraDAO());
		
		setConversione(shortenerServices);
    	setVisitaShortUrl(shortenerServices); 
    	setIspezioneUrl(shortenerServices);
    	setOpzioni();
    }
	
	
	private static void setConversione(ShortenerServices ss) {
		post(API_CONTEXT + API.CONVERT, (request, response) -> {	
			String shortUrl = null;
			
			try {
				shortUrl = ss.conversioneURL(request.body());
				
			} catch(URLExistsException e) {
				System.err.println(e.getMessage());
				response.status(401);
				
			} catch(BadCustomURLException e) {
				System.err.println(e.getMessage());
				response.status(401);
				
			} catch(BadURLException e) {
				System.err.println(e.getMessage());
				response.status(401);
			}
			
			return shortUrl;
    	});
	}
	
	private static void setVisitaShortUrl(ShortenerServices ss) {
    	get("/:goto", (request, response) -> {
    		
    		try {
    			String longUrl = ss.visitaURL(request.params(":goto"), request.ip());
    			response.redirect(longUrl);
    			
    		} catch(ShortURLNotFoundException e) {
    			System.err.println(e.getMessage());
    			response.status(404);
    			//TODO Risolvere il redirect a 404
    		}

    		return null;		
    	});
	}

	private static void setIspezioneUrl(ShortenerServices ss) {
		post(API_CONTEXT + API.STATS, (request, response) -> {
			return ss.ispezionaURL(request.body());
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
