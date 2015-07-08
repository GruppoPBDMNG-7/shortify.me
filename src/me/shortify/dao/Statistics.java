package me.shortify.dao;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Statistics {
	private Map<String, Integer> countryCounters;
	private Map<Date, Integer> dayCounters;
	private Map<Date, Integer> hourCounters;
	private int uniqueCounter;
	
	
	public Statistics(Map<String, Integer> countryCounters,
			Map<Date, Integer> dayCounters,
			Map<Date, Integer> hourCounters, int uniqueCounter) {
		this.countryCounters = countryCounters;
		this.hourCounters = hourCounters;
		this.dayCounters = dayCounters;
		this.uniqueCounter = uniqueCounter;
	}
	
	public Map<String, Integer> getCountryCounters() {
		return countryCounters;
	}
	
	public Map<Date, Integer> getDayCounters() {
		return dayCounters;
	}
	
	public Map<Date, Integer> getHourCounters() {
		return hourCounters;
	}
	
	public int getUniqueViews() {
		return uniqueCounter;
	}
}
