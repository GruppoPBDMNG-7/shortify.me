package me.shortify.sparkserver;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.SparkBase.externalStaticFileLocation;

import java.util.Calendar;

import me.shortify.dao.CassandraDAO;
import me.shortify.dao.DAO;
import me.shortify.utils.filter.DomainChecker;
import me.shortify.utils.filter.WordChecker;
import me.shortify.utils.geoLocation.CountryIPInformation;
import me.shortify.utils.shortenerUrl.Algorithm;

import org.json.JSONException;
import org.json.JSONObject;

import com.maxmind.geoip2.exception.AddressNotFoundException;

public class Services {
	private static final String API_CONTEXT = "/api/v1";
	private static final int MAX_COUNT = 9;
	
	public static void setupEndpoints() {		
		setConversione();
    	setVisitaShortUrl(); 
    	setIspezioneUrl();
    	setOpzioni();
    }
	
	
	private static void setConversione() {
		post(API_CONTEXT + API.CONVERT, (request, response) -> {		
    		String json = request.body();
    		System.out.println("Parametro passato: " + json) ;
    		JSONObject jsonObject = new JSONObject(json);
    		
    		//si ottiene il long url dalla richiesta
    		String url = jsonObject.getString("longurl");
    		
			DomainChecker dc = new DomainChecker();
			WordChecker wc = new WordChecker();
			
    		if (!dc.isBadDomain(url)) {
    			String customText;
        		String shortUrl = "";
        		
        		try {
        			
        			//se e' stato inserito un custom text
        			customText = jsonObject.getString("customText");
        		} catch(JSONException e) {
        			customText = "";
        		}
        		
        		DAO dao = new CassandraDAO();
        		
        		if(customText.equals("")) {  			
        			int numTentativi = 0;
        			
        			do {
    		    		//creazione dell'url
    		    		shortUrl = Algorithm.buildShortUrl(url);
    		    		numTentativi++;
    		    		
        			} while (dao.checkUrl(shortUrl) && numTentativi < MAX_COUNT);
    	    		
    	    		jsonObject = new JSONObject();    		
    	    		System.out.println("Risultato conversione: " + shortUrl);
    	    		
        		} else {
        			if (!wc.isBadWord(customText)) {
        				shortUrl = customText;
    	    			System.out.println("Custom URL inserito: " + shortUrl);
        			} else {
            			System.out.println("Custom text non accettato");
            			response.status(401);
            			return null;
            		}	
        		}
        				
        		if (!dao.checkUrl(shortUrl)) {
        			dao.putUrl(shortUrl, url);
        			
        			//json con short url
            		jsonObject.put("shortUrl", shortUrl);
        		} else {
        			System.out.println("ShortUrl già presente nel DB");
        			response.status(401);
        		}
        		
    		} else {  			
    			System.out.println("Url maligno, non accettato");
    			response.status(401);
    		}
    		
    		
    			
    	    return jsonObject.toString();
    	});
	}
	
	private static void setVisitaShortUrl() {
    	get("/:goto", (request, response) -> {
    		String shortUrl = request.params(":goto"); 
    		String ip = request.ip();
    		String country = "";
    		System.out.println("Valore di shortUrl: " + shortUrl);
    		
    		CountryIPInformation cIPi = new CountryIPInformation();
    		
    		try {
    			country = cIPi.getCountry(ip);
    		} catch (AddressNotFoundException e) {
    			country = "NULL";
    		}
    		
    		CassandraDAO d = new CassandraDAO();	
    		String longUrl = d.getUrl(shortUrl);
    		
    		System.out.println("IP: " + ip);
    		System.out.println("Country: " +  country);
    		System.out.println("Long Url:" + longUrl);
    		
    		if (longUrl != "") {  		
    			response.redirect(longUrl);   		
    		} else {
    			response.status(404);
    		}
    		
    		d.updateUrlStatistics(shortUrl, country, ip, Calendar.getInstance());
    		
    	    return null;
    	});
	}

	private static void setIspezioneUrl() {
		post(API_CONTEXT + API.STATS, (request, response) -> {
			String jsonUrl = request.body();
			JSONObject json = new JSONObject(jsonUrl);
			
			String url = json.getString("shorturl");
			String[] urlParts = url.split("/");
			
			String shortUrl = urlParts[urlParts.length - 1];

			DAO dao = new CassandraDAO();
			json = dao.getStatistics(shortUrl).toJson();
			
			return json.toString();
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
