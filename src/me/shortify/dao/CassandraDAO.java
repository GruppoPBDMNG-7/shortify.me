package me.shortify.dao;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class CassandraDAO implements DAO{
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
	
	@Override
	public boolean checkUrl(String shortUrl) {
		boolean exists = false;
		
		ResultSet rs = session.execute("SELECT " + CassandraSchema.URLS_SHORT_URL_COLUMN + " FROM " 
				+ CassandraSchema.URLS_TABLE + " WHERE " 
				+ CassandraSchema.URLS_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
		
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
	
	@Override
	public void putUrl(String shortUrl, String longUrl) {
		session.execute("INSERT INTO " + CassandraSchema.URLS_TABLE 
				+ "(" + CassandraSchema.URLS_SHORT_URL_COLUMN + ", " 
				+ CassandraSchema.URLS_LONG_URL_COLUMN + ") "
				+ "VALUES ('" + shortUrl + "', '" + longUrl + "');");
	}
	
	public String getUrl(String shortUrl, String country, String ip, Calendar date) {
		String longUrl = "";
		
		ResultSet rs = session.execute("SELECT * FROM " 
				+ CassandraSchema.URLS_TABLE + " WHERE " 
				+ CassandraSchema.URLS_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
		
		for (Row r : rs) {
			longUrl = r.getString(CassandraSchema.URLS_LONG_URL_COLUMN);
		}

		updateCountryCounter(shortUrl, country);
		updateDayCounter(shortUrl, date);
		updateHourCounter(shortUrl, date);		
		updateUniqueCounter(shortUrl, ip);
		
		return longUrl;
	}
	
	private void updateCountryCounter(String shortUrl, String country) {
		session.execute("UPDATE " + CassandraSchema.COUNTRY_COUNTERS_TABLE 
				+ " SET " + CassandraSchema.CC_VALUE_COLUMN + " = " 
				+ CassandraSchema.CC_VALUE_COLUMN + " + 1 "
				+ "WHERE " + CassandraSchema.CC_COUNTRY_COLUMN +" = '" + country + "' "
				+ "AND " + CassandraSchema.CC_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
	}
	
	private void updateDayCounter(String shortUrl, Calendar date) {
		String sDate = date.get(Calendar.YEAR) + "-"
				+ (date.get(Calendar.MONTH) + 1) + "-"
				+ date.get(Calendar.DAY_OF_MONTH);
		session.execute("UPDATE " + CassandraSchema.DAY_COUNTERS_TABLE 
				+ " SET " + CassandraSchema.DC_VALUE_COLUMN + " = " 
				+ CassandraSchema.DC_VALUE_COLUMN + " + 1 "
				+ "WHERE " + CassandraSchema.DC_DAY_COLUMN + " = '" + sDate + "' "
				+ "AND " + CassandraSchema.DC_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
	}
	
	private void updateHourCounter(String shortUrl, Calendar date) {
		String sDate = date.get(Calendar.YEAR) + "-"
				+ (date.get(Calendar.MONTH) + 1) + "-"
				+ date.get(Calendar.DAY_OF_MONTH) + " "
				+ date.get(Calendar.HOUR_OF_DAY) + ":"
				+ "00";
		session.execute("UPDATE " + CassandraSchema.HOUR_COUNTERS_TABLE 
				+ " SET " + CassandraSchema.HC_VALUE_COLUMN 
				+ " = " + CassandraSchema.HC_VALUE_COLUMN + " + 1 "
				+ "WHERE " + CassandraSchema.HC_HOUR_COLUMN + " = '" + sDate + "' "
				+ "AND " + CassandraSchema.HC_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
	}
	
	private void updateUniqueCounter(String shortUrl, String ip) {
		ResultSet rs = session.execute("SELECT ip FROM " 
				+ CassandraSchema.UNIQUE_COUNTER_IPS_TABLE + " WHERE " 
				+ CassandraSchema.UCI_SHORT_URL_COLUMN + " = '" 
				+ shortUrl + "' AND " + CassandraSchema.UCI_IP_COLUMN 
				+ " = '" + ip + "';");
		List<Row> rows = rs.all();
		
		if (rows.size() == 0) {
			/*Se l'ip non è stato trovato allora verrà inserito, e il contatore
			  verrà incrementato */
			session.execute("UPDATE " + CassandraSchema.UNIQUE_COUNTER_TABLE
					+ " SET " + CassandraSchema.UC_VALUE_COLUMN + " = " 
					+ CassandraSchema.UC_VALUE_COLUMN + " + 1 WHERE " 
					+ CassandraSchema.UC_SHORT_URL_COLUMN + " = '" + shortUrl + "';");
			session.execute("INSERT INTO " 
					+ CassandraSchema.UNIQUE_COUNTER_IPS_TABLE 
					+ "(" + CassandraSchema.UCI_SHORT_URL_COLUMN + ", " 
					+ CassandraSchema.UCI_IP_COLUMN + ") VALUES "
					+ "('" + shortUrl +"', '" + ip + "');");
		}

	}
	
	public void close() {
		cluster.close();
		session = null;
	}

	@Override
	public Statistics getStatistics(String shortUrl) {
		Map<String, Integer> mCountry = new HashMap<String, Integer>();
		Map<Date, Integer> mDay = new HashMap<Date, Integer>();
		Map<Date, Integer> mHour = new HashMap<Date, Integer>();
		int unique = 0;
		ResultSet rsCountry = session.execute("SELECT * FROM " 
				+ CassandraSchema.COUNTRY_COUNTERS_TABLE 
				+ " WHERE " + CassandraSchema.CC_SHORT_URL_COLUMN + " = "
						+ "'" + shortUrl + "';");
		ResultSet rsDay = session.execute("SELECT * FROM " 
						+ CassandraSchema.DAY_COUNTERS_TABLE 
						+ " WHERE " + CassandraSchema.DC_SHORT_URL_COLUMN + " = "
								+ "'" + shortUrl + "';");
		ResultSet rsHour = session.execute("SELECT * FROM " 
				+ CassandraSchema.HOUR_COUNTERS_TABLE 
				+ " WHERE " + CassandraSchema.HC_SHORT_URL_COLUMN + " = "
						+ "'" + shortUrl + "';");
		ResultSet rsUnique = session.execute("SELECT * FROM " 
						+ CassandraSchema.UNIQUE_COUNTER_TABLE 
						+ " WHERE " + CassandraSchema.UC_SHORT_URL_COLUMN + " = "
				+ "'" + shortUrl + "';");
		
		for (Row r : rsCountry) {
			mCountry.put(r.getString(CassandraSchema.CC_COUNTRY_COLUMN), 
					(int) r.getLong(CassandraSchema.CC_VALUE_COLUMN));
		}
		for (Row r : rsDay) {
			mDay.put(r.getDate(CassandraSchema.DC_DAY_COLUMN), 
					(int) r.getLong(CassandraSchema.DC_VALUE_COLUMN));
		}
		for (Row r : rsHour) {
			mHour.put(r.getDate(CassandraSchema.HC_HOUR_COLUMN), 
					(int) r.getLong(CassandraSchema.HC_VALUE_COLUMN));
		}
		for (Row r : rsUnique) {
			unique = (int) r.getLong(CassandraSchema.UC_VALUE_COLUMN);
		}
		
		return new Statistics(mCountry, mDay, mHour, unique);
	}
}
