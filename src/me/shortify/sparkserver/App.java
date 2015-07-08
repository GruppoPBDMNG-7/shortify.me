package me.shortify.sparkserver;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class App {
	
    public static void main( String[] args ) {
    	
    	/*post("/convert", (request, response) -> {
    		
    		String json = request.body();
    		System.out.println("Parametro passato: " + json) ;
    		JSONObject jsonObject = new JSONObject(json);
    		
    		//si ottiene il long url dalla richiesta
    		String url = jsonObject.getString("longurl");
    		
    		String customText;
    		String shortUrl = "";
    		
    		try {
    			
    			//se e' stato inserito un custom text
    			customText = jsonObject.getString("customText");
    		} catch(JSONException e) {
    			customText = "";
    		}
    		
    		CassandraDAO d = new CassandraDAO();
    		
    		if(customText == "") { 
    			
	    		//conversione dell'url
	    		//shortUrl = Converter.convert(url);
	    		jsonObject = new JSONObject();
	    		
    		} else {
    			
    			//controlli richiesti
    			//shortUrl = Converter.insertCustomUrl(url, customText);
    		}
    		
    		System.out.println("Risultato conversione: " + shortUrl);
    		
    		if (!d.checkUrl(shortUrl)) {
    			d.putUrl(shortUrl, url);
    			
    			//json con short url
        		jsonObject.put("shortUrl", shortUrl);
    		} else {
    			
    			//json con messaggio di errore
    			jsonObject.put("error", "An error occured!");
    		}
    			
    	    return jsonObject.toString();
    	});*/
    	
    	
    	get("/:goto", (request, response) -> {
    		String shortUrl = request.params(":goto"); 
		
    		System.out.println("Valore di shortUrl: " + shortUrl);
    		
    		//CassandraDAO d = new CassandraDAO();	
    		//String longUrl = d.getUrl(shortUrl, "IT", request.ip(), Calendar.getInstance());
    		
    		//System.out.println("Long Url:" + longUrl);
    		
    		//response.redirect(longUrl);
    	    return null;
    	});
    	
    	
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
