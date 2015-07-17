import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;

import me.shortify.dao.Statistics;
import junit.framework.TestCase;


public class StatisticsTest extends TestCase {
	private Map<String, Long> countryCounters = new HashMap<String, Long>();
	private Map<Date, Long> dayCounters = new HashMap<Date, Long>();
	private Map<Date, Long> hourCounters = new HashMap<Date, Long>();
	
	private String[][] countries = {
			{"IT", "DE"},
			{"IT", "DE"},
			{"IT"},
			{}
	};
	private long[][] cCounters = {
			{5, 8},
			{9, 12},
			{17}
	};
	
	private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private String[][] days = {
			{"2015-07-17", "2015-07-16"},
			{"2015-06-17", "2015-06-16"},
			{"2015-07-17"},
			{}
	};
	private long[][] dCounters = {
			{4, 9},
			{5, 16},
			{17}
	};
	
	private SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private String[][] hours = {
			{"2015-07-17 09:00", "2015-07-17 08:00", "2015-07-16 13:00", "2015-07-16 12:00"},
			{"2015-06-17 16:00", "2015-06-16 19:00", "2015-06-16 18:00"},
			{"2015-07-17 07:00"},
			{}
	};
	private long[][] hCounters = {
			{2, 2, 4, 5},
			{5, 8, 8},
			{17}
	};
	
	private long[] uniqueCounters = {
			5,
			2,
			8,
			0
	};

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		countryCounters = new HashMap<String, Long>();
		dayCounters = new HashMap<Date, Long>();
		hourCounters = new HashMap<Date, Long>();
	}
	
	public void testToJson() throws ParseException {
		//Test cases 1-3: non-empty statistics
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < countries[i].length; j++) {
				countryCounters.put(countries[i][j], cCounters[i][j]);
			}
			for (int j = 0; j < days[i].length; j++) {
				String day = days[i][j];
				dayCounters.put(dayFormat.parse(day), dCounters[i][j]);
			}
			for (int j = 0; j < hours[i].length; j++) {
				hourCounters.put(hourFormat.parse(hours[i][j]), hCounters[i][j]);
			}
			Statistics s = new Statistics("testLong", "testShort", countryCounters, dayCounters, hourCounters, uniqueCounters[i]);
			
			JSONObject json = s.toJson();
			String shortUrl = json.getString(Statistics.SHORT_URL_FIELD);
			assertTrue("Expected 'testShort', got " + shortUrl, shortUrl.equals("testShort"));
			String longUrl = json.getString(Statistics.LONG_URL_FIELD);
			assertTrue("Expected 'testLong', got " + longUrl, longUrl.equals("testLong"));
			long uniqueCounter = json.getLong(Statistics.UNIQUE_COUNTER_FIELD);
			assertTrue("Expected " + uniqueCounters[i] +", got " + uniqueCounter, uniqueCounter == uniqueCounters[i]);
			
			JSONObject cc = json.getJSONObject(Statistics.COUNTRY_COUNTERS_FIELD);
			for (int j = 0; j < countries[i].length; j++) {
				long counter = cc.getLong(countries[i][j]);
				assertTrue("Expected " + cCounters[i][j] + ", got " + counter, counter == cCounters[i][j]);
			}
			
			JSONObject dd = json.getJSONObject(Statistics.DAY_COUNTERS_FIELD);
			for (int j = 0; j < days[i].length; j++) {
				long counter = dd.getLong(days[i][j]);
				assertTrue("Expected " + dCounters[i][j] + ", got " + counter, counter == dCounters[i][j]);
			}
			
			JSONObject hh = json.getJSONObject(Statistics.HOUR_COUNTERS_FIELD);
			for (int j = 0; j < hours[i].length; j++) {
				long counter = hh.getLong(hours[i][j]);
				assertTrue("Expected " + hCounters[i][j] + ", got " + counter, counter == hCounters[i][j]);
			}
		}
		
		//Test case 4: empty data
		countryCounters = new HashMap<String, Long>();
		dayCounters = new HashMap<Date, Long>();
		hourCounters = new HashMap<Date, Long>();
		
		for (int j = 0; j < countries[3].length; j++) {
			countryCounters.put(countries[3][j], cCounters[3][j]);
		}
		for (int j = 0; j < days[3].length; j++) {
			String day = days[3][j];
			dayCounters.put(dayFormat.parse(day), dCounters[3][j]);
		}
		for (int j = 0; j < hours[3].length; j++) {
			hourCounters.put(hourFormat.parse(hours[3][j]), hCounters[3][j]);
		}
		
		Statistics s = new Statistics("testLong", "testShort", countryCounters, dayCounters, hourCounters, 0);
		
		JSONObject json = s.toJson();
		String shortUrl = json.getString(Statistics.SHORT_URL_FIELD);
		assertTrue("Expected 'testShort', got " + shortUrl, shortUrl.equals("testShort"));
		String longUrl = json.getString(Statistics.LONG_URL_FIELD);
		assertTrue("Expected 'testLong', got " + longUrl, longUrl.equals("testLong"));
		long uniqueCounter = json.getLong(Statistics.UNIQUE_COUNTER_FIELD);
		assertTrue("Expected " + uniqueCounters[3] +", got " + uniqueCounter, uniqueCounter == uniqueCounters[3]);
		
		JSONObject cc = json.getJSONObject(Statistics.COUNTRY_COUNTERS_FIELD);
		assertTrue("countryCounters is not empty!", cc.toString().equals("{}"));
		
		JSONObject dd = json.getJSONObject(Statistics.DAY_COUNTERS_FIELD);
		assertTrue("dayCounters is not empty!", dd.toString().equals("{}"));
		
		JSONObject hh = json.getJSONObject(Statistics.HOUR_COUNTERS_FIELD);
		assertTrue("hourCounters is not empty!", hh.toString().equals("{}"));
	}

}
