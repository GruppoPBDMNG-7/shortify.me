package me.shortify.dao;
import java.util.Calendar;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class CassandraDAO {
	private Cluster cluster;
	private Session session;
	
	public CassandraDAO() {
		initialize();
	}
	
	public void initialize() {	
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect();
		
		session.execute(CassandraSchema.CREATE_KEYSPACE);
		
		session = cluster.connect(CassandraSchema.KEYSPACE_NAME);
		session.execute(CassandraSchema.CREATE_URLS);
		session.execute(CassandraSchema.CREATE_COUNTRY_COUNTERS);
		session.execute(CassandraSchema.CREATE_DAY_COUNTERS);
		session.execute(CassandraSchema.CREATE_HOUR_COUNTERS);
		session.execute(CassandraSchema.CREATE_UNIQUE_COUNTER);
		session.execute(CassandraSchema.CREATE_UNIQUE_COUNTER_IPS);
	}
	
	public boolean checkUrl(String shortUrl) {
		boolean exists = false;
		
		ResultSet rs = session.execute("SELECT short_url FROM " + CassandraSchema.URLS_TABLE + " WHERE short_url = '" + shortUrl + "';");
		
		try {
			//Se c'è almeno una riga viene eseguito, quindi l'url esiste gi�
			for (Row r : rs) {
				exists = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return exists;
	}
	
	public void putUrl(String shortUrl, String longUrl) {
		session.execute("INSERT INTO " + CassandraSchema.URLS_TABLE 
				+ "(short_url, long_url) "
				+ "VALUES ('" + shortUrl + "', '" + longUrl + "');");
	}
	
	public String getUrl(String shortUrl, String country, String ip, Calendar date) {
		String longUrl = "";
		
		ResultSet rs = session.execute("SELECT * FROM " + CassandraSchema.URLS_TABLE + " WHERE short_url = '" + shortUrl + "';");
		
		for (Row r : rs) {
			longUrl = r.getString("long_url");
		}

		updateCountryCounter(shortUrl, country);
		updateDayCounter(shortUrl, date);
		updateHourCounter(shortUrl, date);		
		updateUniqueCounter(shortUrl, ip);
		
		return longUrl;
	}
	
	private void updateCountryCounter(String shortUrl, String country) {
		session.execute("UPDATE " + CassandraSchema.COUNTRY_COUNTERS_TABLE 
				+ " SET value = value + 1 "
				+ "WHERE country = '" + country + "' "
				+ "AND short_url = '" + shortUrl + "';");
	}
	
	private void updateDayCounter(String shortUrl, Calendar date) {
		String sDate = date.get(Calendar.YEAR) + "-"
				+ (date.get(Calendar.MONTH) + 1) + "-"
				+ date.get(Calendar.DAY_OF_MONTH);
		session.execute("UPDATE " + CassandraSchema.DAY_COUNTERS_TABLE 
				+ " SET value = value + 1 "
				+ "WHERE day = '" + sDate + "' "
				+ "AND short_url = '" + shortUrl + "';");
	}
	
	private void updateHourCounter(String shortUrl, Calendar date) {
		String sDate = date.get(Calendar.YEAR) + "-"
				+ (date.get(Calendar.MONTH) + 1) + "-"
				+ date.get(Calendar.DAY_OF_MONTH) + " "
				+ date.get(Calendar.HOUR_OF_DAY) + ":"
				+ "00";
		session.execute("UPDATE " + CassandraSchema.HOUR_COUNTERS_TABLE 
				+ " SET value = value + 1 "
				+ "WHERE hour = '" + sDate + "' "
				+ "AND short_url = '" + shortUrl + "';");
	}
	
	private void updateUniqueCounter(String shortUrl, String ip) {
		ResultSet rs = session.execute("SELECT ip FROM " 
				+ CassandraSchema.UNIQUE_COUNTER_IPS_TABLE + " WHERE short_url = '" 
				+ shortUrl + "' AND ip = '" + ip + "';");
		List<Row> rows = rs.all();
		
		if (rows.size() == 0) {
			/*Se l'ip non è stato trovato allora verrà inserito, e il contatore
			  verrà incrementato */
			session.execute("UPDATE " + CassandraSchema.UNIQUE_COUNTER_TABLE
					+ " SET value = value + 1 WHERE short_url = '" + shortUrl + "';");
			session.execute("INSERT INTO " 
					+ CassandraSchema.UNIQUE_COUNTER_IPS_TABLE 
					+ "(short_url, ip) VALUES "
					+ "('" + shortUrl +"', '" + ip + "');");
		}

	}
	
	public void close() {
		cluster.close();
		session = null;
	}
}
