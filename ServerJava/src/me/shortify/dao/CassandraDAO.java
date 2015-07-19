package me.shortify.dao;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class CassandraDAO implements DAO{
	private Cluster cluster;
	private Session session;
	
	private PreparedStatement checkUrlPS;
	private PreparedStatement putUrlPS;
	private PreparedStatement updateCountryCounterPS;
	private PreparedStatement updateDayCounterPS;
	private PreparedStatement updateHourCounterPS;
	private PreparedStatement updateUniqueCounterPS;
	private PreparedStatement checkIpPS;
	private PreparedStatement insertIpPS;
	private PreparedStatement getCountryCountersPS;
	private PreparedStatement getDayCountersPS;
	private PreparedStatement getHourCountersPS;
	private PreparedStatement getUniqueCounterPS;
	private PreparedStatement getUrlPS;
	
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
		
		if (checkUrlPS == null) {
			checkUrlPS = session.prepare(CassandraSchema.CHECK_URL_QUERY);
		}
		
		BoundStatement bs = checkUrlPS.bind(shortUrl);
		ResultSet rs = session.execute(bs);
		
		try {
			//Se c'e' almeno una riga viene eseguito, quindi l'url esiste gia'
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
		if (putUrlPS == null) {
			putUrlPS = session.prepare(CassandraSchema.PUT_URL_QUERY);
		}
		
		BoundStatement bs = putUrlPS.bind(shortUrl, longUrl);
		session.execute(bs);
	}
	
	@Override
	public void updateUrlStatistics(String shortUrl, String country, String ip, Calendar date) {
		updateCountryCounter(shortUrl, country);
		updateDayCounter(shortUrl, date);
		updateHourCounter(shortUrl, date);		
		updateUniqueCounter(shortUrl, ip);		
	}
	
	private void updateCountryCounter(String shortUrl, String country) {
		if (updateCountryCounterPS == null) {
			updateCountryCounterPS = session.prepare(CassandraSchema.UPDATE_COUNTRY_COUNTER_QUERY);
		}
		BoundStatement bs = updateCountryCounterPS.bind(country, shortUrl);
		session.execute(bs);
	}
	
	private void updateDayCounter(String shortUrl, Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		if (updateDayCounterPS == null) {
			updateDayCounterPS = session.prepare(CassandraSchema.UPDATE_DAY_COUNTER_QUERY);
		}
		BoundStatement bs = updateDayCounterPS.bind(date.getTime(), shortUrl);
		session.execute(bs);
	}
	
	private void updateHourCounter(String shortUrl, Calendar date) {
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		if (updateHourCounterPS == null) {
			updateHourCounterPS = session.prepare(CassandraSchema.UPDATE_HOUR_COUNTER_QUERY);
		}
		BoundStatement bs = updateHourCounterPS.bind(date.getTime(), shortUrl);
		session.execute(bs);
	}
	
	private void updateUniqueCounter(String shortUrl, String ip) {
		if (checkIpPS == null) {
			checkIpPS = session.prepare(CassandraSchema.CHECK_IP_QUERY);
		}
		BoundStatement bs = checkIpPS.bind(shortUrl, ip);
		session.execute(bs);
		ResultSet rs = session.execute(bs);
		List<Row> rows = rs.all();
		
		if (rows.size() == 0) {
			/* If the IP address is not found the unique counter is incremented,
			 * and the IP address is inserted in the table. */
			if (updateUniqueCounterPS == null) {
				updateUniqueCounterPS = session.prepare(CassandraSchema.UPDATE_UNIQUE_COUNTER_QUERY);
			}
			bs = updateUniqueCounterPS.bind(shortUrl);
			session.execute(bs);
			
			if (insertIpPS == null) {
				insertIpPS = session.prepare(CassandraSchema.INSERT_IP_QUERY);
			}
			bs = insertIpPS.bind(shortUrl, ip);
			session.execute(bs);
		}

	}
	
	public void close() {
		cluster.close();
		session = null;
	}

	@Override
	public Statistics getStatistics(String shortUrl) {
		String longUrl = getUrl(shortUrl);
		Map<String, Long> mCountry = new HashMap<String, Long>();
		Map<Date, Long> mDay = new HashMap<Date, Long>();
		Map<Date, Long> mHour = new HashMap<Date, Long>();
		long unique = 0;
		
		if (getCountryCountersPS == null 
				|| getDayCountersPS == null || getHourCountersPS == null 
				|| getUniqueCounterPS == null) {
			getCountryCountersPS = session.prepare(CassandraSchema.GET_COUNTRY_COUNTERS_QUERY);
			getDayCountersPS = session.prepare(CassandraSchema.GET_DAY_COUNTERS_QUERY);
			getHourCountersPS = session.prepare(CassandraSchema.GET_HOUR_COUNTERS_QUERY);
			getUniqueCounterPS = session.prepare(CassandraSchema.GET_UNIQUE_COUNTER_QUERY);
		}
		
		BoundStatement bs = getCountryCountersPS.bind(shortUrl);
		ResultSet rsCountry = session.execute(bs);
		
		bs = getDayCountersPS.bind(shortUrl);
		ResultSet rsDay = session.execute(bs);
		
		bs = getHourCountersPS.bind(shortUrl);
		ResultSet rsHour = session.execute(bs);
		
		bs = getUniqueCounterPS.bind(shortUrl);
		ResultSet rsUnique = session.execute(bs);
		
		for (Row r : rsCountry) {
			mCountry.put(r.getString(CassandraSchema.CC_COUNTRY_COLUMN), 
					r.getLong(CassandraSchema.CC_VALUE_COLUMN));
		}
		for (Row r : rsDay) {
			mDay.put(r.getDate(CassandraSchema.DC_DAY_COLUMN), 
					r.getLong(CassandraSchema.DC_VALUE_COLUMN));
			System.out.println(r.getDate(CassandraSchema.DC_DAY_COLUMN));
		}
		for (Row r : rsHour) {
			mHour.put(r.getDate(CassandraSchema.HC_HOUR_COLUMN), 
					r.getLong(CassandraSchema.HC_VALUE_COLUMN));
		}
		for (Row r : rsUnique) {
			unique = r.getLong(CassandraSchema.UC_VALUE_COLUMN);
		}
		
		return new Statistics(longUrl, shortUrl, mCountry, mDay, mHour, unique);
	}

	@Override
	public String getUrl(String shortUrl) {
		String longUrl = "";
		if (getUrlPS == null) {
			getUrlPS = session.prepare(CassandraSchema.GET_URL_QUERY);
		}
		BoundStatement bs = getUrlPS.bind(shortUrl);
		ResultSet rs = session.execute(bs);
		
		for (Row r : rs) {
			longUrl = r.getString(CassandraSchema.URLS_LONG_URL_COLUMN);
		}
		
		return longUrl;
	}
	
	//THIS IS A TEST-ONLY METHOD! NEVER USE IT!
	public void resetDB(){
		session.execute("DROP KEYSPACE "+ CassandraSchema.KEYSPACE_NAME+" ;");
	}
}
