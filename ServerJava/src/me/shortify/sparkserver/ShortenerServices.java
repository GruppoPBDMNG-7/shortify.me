package me.shortify.sparkserver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;

import me.shortify.dao.DAO;
import me.shortify.sparkserver.exception.BadCustomURLException;
import me.shortify.sparkserver.exception.BadURLException;
import me.shortify.sparkserver.exception.ShortURLNotFoundException;
import me.shortify.sparkserver.exception.URLExistsException;
import me.shortify.utils.filter.DomainChecker;
import me.shortify.utils.filter.WordChecker;
import me.shortify.utils.geoLocation.CountryIPInformation;
import me.shortify.utils.shortenerUrl.Algorithm;

import org.json.JSONException;
import org.json.JSONObject;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

public class ShortenerServices {
	
	private static final int MAX_COUNT = 10;
	
	private DAO dao;
	
	public ShortenerServices(DAO dao) {
		this.dao = dao;
	}
	
	public String conversioneURL(String jsonURL) {		
		JSONObject jsonObject = new JSONObject(jsonURL);
		
		//si ottiene il long url dalla richiesta
		String url = jsonObject.getString("longurl");
		
		//controllo se l'url inizia con http:// o https://
		if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://"+url;
        } 		
		
		DomainChecker dc = new DomainChecker();	
		if (!dc.isBadDomain(url)) {
			String customText;
    		String shortUrl = "";
    		
    		try {
    			
    			//se e' stato inserito un custom text
    			customText = jsonObject.getString("customText");
    		} catch(JSONException e) {
    			customText = "";
    		}
    		
    		//reset json di risposta
    		jsonObject = new JSONObject();
    		
    	
    		if(customText.equals("")) {		//se non e' stato inserito un custom text		
    			int numTentativi = 0;
    			
    			do {
		    		//creazione dell'url
		    		shortUrl = Algorithm.buildShortUrl(url);
		    		numTentativi++;
		    		
    			} while (dao.checkUrl(shortUrl) && numTentativi < MAX_COUNT);
	    		
    			//sono stati fatti troppi tentativi?
    			if (numTentativi <= MAX_COUNT) {
    				dao.putUrl(shortUrl, url);
        			
        			//json con short url
            		jsonObject.put("shortUrl", shortUrl);
    	    		System.out.println("Risultato conversione: " + shortUrl);
    
        		} else {
        			throw new URLExistsException("ShortUrl già presente nel DB");
        		}
    		
    		} else {	 //e' stato inserito un custom text
    			
    			WordChecker wc = new WordChecker();
    			if (!wc.isBadWord(customText)) {
    				shortUrl = customText;
    				if(!dao.checkUrl(shortUrl)) {
    					dao.putUrl(shortUrl, url);
    					
    					//json con short url
    					jsonObject.put("shortUrl", shortUrl);
    					System.out.println("Custom URL inserito: " + shortUrl);
    				} else {
    					throw new URLExistsException("ShortUrl già presente nel DB");
    				} 		
    			} else {
        			throw new BadCustomURLException("Custom text non accettato");
        		}	
    		}
    						
		} else {  			
			throw new BadURLException("Url maligno, non accettato");
		}
		
		return jsonObject.toString();
	}
	
	public String visitaURL(String shortUrl, String ip) 
			throws IOException, URISyntaxException, GeoIp2Exception {
		
		String country = "";
		
		System.out.println("Valore di shortUrl: " + shortUrl);
		
		CountryIPInformation cIPi = new CountryIPInformation();
		
		try {		
			country = cIPi.getCountry(ip);
		} catch (AddressNotFoundException e) {
			country = "NULL";
		} 
		
		//si ottiene il long URL, se non presente si ha stringa vuota
		String longUrl = dao.getUrl(shortUrl);
		
		System.out.println("IP: " + ip);
		System.out.println("Country: " +  country);
		System.out.println("Long Url:" + longUrl);
		
		if (!longUrl.equals("")) {  		
			dao.updateUrlStatistics(shortUrl, country, ip, Calendar.getInstance());
		} else {
			throw new ShortURLNotFoundException("Short URL non presente nel DB");
		}
		
	    return longUrl;
	}
	
	public String ispezionaURL(String jsonUrl) {
		//TODO da inserire controlli
		JSONObject json = new JSONObject(jsonUrl);
		
		String url = json.getString("shorturl");
		String[] urlParts = url.split("/");
		
		String shortUrl = urlParts[urlParts.length - 1];

		json = dao.getStatistics(shortUrl).toJson();
		
		return json.toString();
	}
}
