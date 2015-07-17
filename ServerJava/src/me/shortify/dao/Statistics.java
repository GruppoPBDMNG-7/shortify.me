package me.shortify.dao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;

public class Statistics {
	private String longUrl;
	private String shortUrl;
	private Map<String, Long> countryCounters;
	private Map<Date, Long> dayCounters;
	private Map<Date, Long> hourCounters;
	private long uniqueCounter;
	
	
	public Statistics(String longUrl, String shortUrl, Map<String, Long> countryCounters,
			Map<Date, Long> dayCounters,
			Map<Date, Long> hourCounters, long uniqueCounter) {
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.countryCounters = countryCounters;
		this.hourCounters = hourCounters;
		this.dayCounters = dayCounters;
		this.uniqueCounter = uniqueCounter;
	}
	
	public String getLongUrl() {
		return this.longUrl;
	}
	
	public Map<String, Long> getCountryCounters() {
		return countryCounters;
	}
	
	public Map<Date, Long> getDayCounters() {
		return dayCounters;
	}
	
	public Map<Date, Long> getHourCounters() {
		return hourCounters;
	}
	
	public long getUniqueViews() {
		return uniqueCounter;
	}
	
	public String getShortUrl() {
		return shortUrl;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("shortUrl", shortUrl);
		
		JSONObject countryJS = new JSONObject();
		for (Map.Entry<String, Long> e : countryCounters.entrySet()) {
			countryJS.put(e.getKey(), e.getValue());
		}
		json.put("countryCounters", countryJS);
		
		JSONObject dayJS = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Map.Entry<Date, Long> e : dayCounters.entrySet()) {
			Calendar c = new GregorianCalendar();
			c.setTime(e.getKey());
			c.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdf.setCalendar(c);
			String date = sdf.format(c.getTime());		
			dayJS.put(date, e.getValue());
		}
		json.put("dayCounters", dayJS);
		
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		JSONObject hourJS = new JSONObject();
		for (Map.Entry<Date, Long> e : hourCounters.entrySet()) {
			Calendar c = new GregorianCalendar();
			c.setTime(e.getKey());
			c.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdf.setCalendar(c);
			String date = sdf.format(c.getTime());			
			hourJS.put(date, e.getValue());
		}
		json.put("hourCounters", hourJS);
		
		json.put("uniqueCounter", uniqueCounter);
		
		return json;
	}
}
