package me.shortify.dao;

import java.util.Calendar;

public interface DAO {
	/**
	 * Checks if a short url is present in the database.
	 * @param shortUrl short url to check
	 * @return true if the short url is present, false otherwise
	 */
	
	public boolean checkUrl(String shortUrl);
	/**
	 * Inserts a new short url and long url in the database.
	 * @param shortUrl short url to insert
	 * @param longUrl long url to insert
	 */
	public void putUrl(String shortUrl, String longUrl);
	
	/**
	 * Update the click counters for the selected short url
	 * @param shortUrl the short url to update
	 * @param country country of the visitor
	 * @param ip ip address of the visitor
	 * @param date date and time of the click
	 */
	public void updateUrlStatistics(String shortUrl, String country, String ip, Calendar date);
	
	/**
	 * Retrieves the long url of the associated short url.
	 * @param shortUrl the short url
	 * @return
	 */
	public String getUrl(String shortUrl);
	
	/**
	 * Returns the object containing the statistics for the selected short url.
	 * @param shortUrl short url of which the statistics will be retrieved.
	 * @param date date and time of the request
	 * @param nOfHours number of hours previous of the current date, it is
	 * 		  used to determined from when to start retrieving the counters by hour. 
	 * 		  For example, if the current time is 2015-07-20 8:00, 
	 * 		  and the parameter is 24, all hour counters starting
	 * 	      from 2015-07-19 8:00 will be retrieved.
	 * 		  Note: if nOfHours is set to 0, all hour counters will be retrieved.
	 * @return
	 */
	public Statistics getStatistics(String shortUrl, Calendar date, int nOfHours);
	
	/**
	 * Closes the DB connections and frees the used resources.
	 */
	public void close();
}
