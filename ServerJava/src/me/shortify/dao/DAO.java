package me.shortify.dao;

import java.util.Calendar;

public interface DAO {
	public boolean checkUrl(String shortUrl);
	public void putUrl(String shortUrl, String longUrl);
	public void updateUrlStatistics(String shortUrl, String country, String ip, Calendar date);
	public String getUrl(String shortUrl);
	public Statistics getStatistics(String shortUrl);
}
