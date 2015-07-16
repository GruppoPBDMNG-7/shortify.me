package me.shortify.dao;

public class CassandraSchema {
	public static final String KEYSPACE_NAME = "urlshortener";
	
	public static final String URLS_TABLE = "urls";
	public static final String URLS_SHORT_URL_COLUMN = "short_url";
	public static final String URLS_LONG_URL_COLUMN = "long_url";
	
	public static final String COUNTRY_COUNTERS_TABLE = "countrycounters";
	public static final String CC_SHORT_URL_COLUMN = "short_url";
	public static final String CC_COUNTRY_COLUMN = "country";
	public static final String CC_VALUE_COLUMN = "value";
	
	public static final String DAY_COUNTERS_TABLE = "daycounters";
	public static final String DC_SHORT_URL_COLUMN = "short_url";
	public static final String DC_DAY_COLUMN = "day";
	public static final String DC_VALUE_COLUMN = "value";
	
	public static final String HOUR_COUNTERS_TABLE = "hourcounters";
	public static final String HC_SHORT_URL_COLUMN = "short_url";
	public static final String HC_HOUR_COLUMN = "hour";
	public static final String HC_VALUE_COLUMN = "value";
	
	public static final String UNIQUE_COUNTER_TABLE = "uniquecounter";
	public static final String UC_SHORT_URL_COLUMN = "short_url";
	public static final String UC_VALUE_COLUMN = "value";
	
	public static final String UNIQUE_COUNTER_IPS_TABLE = "uniquecounterips";
	public static final String UCI_SHORT_URL_COLUMN = "short_url";
	public static final String UCI_IP_COLUMN = "ip";
	
	public static final String CREATE_KEYSPACE = "CREATE KEYSPACE IF NOT EXISTS  " + KEYSPACE_NAME + " "
			+ "WITH REPLICATION = { 'class' : 'SimpleStrategy', "
			+ "'replication_factor' : 1 };";
	
	public static final String CREATE_URLS = 
			"CREATE TABLE IF NOT EXISTS " + URLS_TABLE + " "
			+ "(" + URLS_SHORT_URL_COLUMN + " text,"
			+ URLS_LONG_URL_COLUMN +" text,"
			+ "PRIMARY KEY ((" + URLS_SHORT_URL_COLUMN + "))"
			+ ");";
	public static final String CREATE_COUNTRY_COUNTERS = 
			"CREATE TABLE IF NOT EXISTS " + COUNTRY_COUNTERS_TABLE + " "
			+ "(" + CC_SHORT_URL_COLUMN +" text,"
			+ CC_COUNTRY_COLUMN + " text,"
			+ CC_VALUE_COLUMN +" counter,"
			+ "PRIMARY KEY (("+ CC_SHORT_URL_COLUMN +"), " + CC_COUNTRY_COLUMN + ")) "
			+ "WITH caching = '{\"keys\":\"ALL\", \"rows_per_partition\":\"10\"}';";
	
	public static final String CREATE_DAY_COUNTERS =
			"CREATE TABLE IF NOT EXISTS " + DAY_COUNTERS_TABLE + " "
					+ "(" + DC_SHORT_URL_COLUMN + " text,"
					+ DC_DAY_COLUMN +" timestamp,"
					+ DC_VALUE_COLUMN + " counter,"
					+ "PRIMARY KEY ((" + DC_SHORT_URL_COLUMN + "), " + DC_DAY_COLUMN + ")) "
					+ "WITH CLUSTERING ORDER BY (" + DC_DAY_COLUMN + " DESC) "
					+ "AND caching = '{\"keys\":\"ALL\", \"rows_per_partition\":\"10\"}';";;
	
	public static final String CREATE_HOUR_COUNTERS = 
			"CREATE TABLE IF NOT EXISTS " + HOUR_COUNTERS_TABLE + " "
					+ "(" + HC_SHORT_URL_COLUMN + " text,"
					+ HC_HOUR_COLUMN + " timestamp,"
					+ HC_VALUE_COLUMN + " counter,"
					+ "PRIMARY KEY ((" + HC_SHORT_URL_COLUMN + "), " + HC_HOUR_COLUMN + ")) "
					+ "WITH CLUSTERING ORDER BY (" + HC_HOUR_COLUMN + " DESC) "
					+ "AND caching = '{\"keys\":\"ALL\", \"rows_per_partition\":\"10\"}';";
	
	public static final String CREATE_UNIQUE_COUNTER = 
			"CREATE TABLE IF NOT EXISTS " + UNIQUE_COUNTER_TABLE + " "
					+ "(" + UC_SHORT_URL_COLUMN + " text,"
					+ UC_VALUE_COLUMN + " counter,"
					+ "PRIMARY KEY (" + UC_SHORT_URL_COLUMN + "));";
	
	public static final String CREATE_UNIQUE_COUNTER_IPS = 
			"CREATE TABLE IF NOT EXISTS " + UNIQUE_COUNTER_IPS_TABLE + " "
					+ "(" + UCI_SHORT_URL_COLUMN + " text,"
					+ UCI_IP_COLUMN +" text,"
					+ "PRIMARY KEY ((" + UCI_SHORT_URL_COLUMN + "), " + UCI_IP_COLUMN + "));";	
}
