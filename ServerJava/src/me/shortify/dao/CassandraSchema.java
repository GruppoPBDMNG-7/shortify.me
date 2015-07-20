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
	
	public static final String CHECK_URL_QUERY = "SELECT " + URLS_SHORT_URL_COLUMN 
			+ " FROM " + URLS_TABLE 
			+ " WHERE " + URLS_SHORT_URL_COLUMN + " = ?;";
	public static final String PUT_URL_QUERY = "INSERT INTO " + URLS_TABLE 
			+ "(" + URLS_SHORT_URL_COLUMN + ", " 
			+ URLS_LONG_URL_COLUMN + ") "
			+ "VALUES (?,?);";
	public static final String UPDATE_COUNTRY_COUNTER_QUERY = "UPDATE " 
			+ COUNTRY_COUNTERS_TABLE 
			+ " SET " + CC_VALUE_COLUMN + " = " 
			+ CC_VALUE_COLUMN + " + 1 "
			+ "WHERE " + CC_COUNTRY_COLUMN +" = ? "
			+ "AND " + CassandraSchema.CC_SHORT_URL_COLUMN + " = ?;";
	public static final String UPDATE_DAY_COUNTER_QUERY = "UPDATE " + DAY_COUNTERS_TABLE 
			+ " SET " + DC_VALUE_COLUMN + " = " 
			+ DC_VALUE_COLUMN + " + 1 "
			+ "WHERE " + DC_DAY_COLUMN + " = ? "
			+ "AND " + DC_SHORT_URL_COLUMN + " = ?;";
	public static final String UPDATE_HOUR_COUNTER_QUERY = "UPDATE " + HOUR_COUNTERS_TABLE 
			+ " SET " + HC_VALUE_COLUMN 
			+ " = " + HC_VALUE_COLUMN + " + 1 "
			+ "WHERE " + HC_HOUR_COLUMN + " = ?"
			+ "AND " + HC_SHORT_URL_COLUMN + " = ?;";
	public static final String CHECK_IP_QUERY = "SELECT ip FROM " 
			+ UNIQUE_COUNTER_IPS_TABLE + " WHERE " 
			+ UCI_SHORT_URL_COLUMN + " = ? AND " + UCI_IP_COLUMN + " = ?;";
	public static final String UPDATE_UNIQUE_COUNTER_QUERY = "UPDATE " + UNIQUE_COUNTER_TABLE
			+ " SET " + UC_VALUE_COLUMN + " = " 
			+ UC_VALUE_COLUMN + " + 1 WHERE " 
			+ UC_SHORT_URL_COLUMN + " = ?;";
	public static final String INSERT_IP_QUERY = "INSERT INTO " 
			+ UNIQUE_COUNTER_IPS_TABLE 	+ "(" + UCI_SHORT_URL_COLUMN + ", " 
			+ UCI_IP_COLUMN + ") VALUES " + "(?, ?);";
	public static final String GET_COUNTRY_COUNTERS_QUERY = "SELECT * FROM " 
			+ COUNTRY_COUNTERS_TABLE 
			+ " WHERE " + CC_SHORT_URL_COLUMN + " = ?;";
	public static final String GET_DAY_COUNTERS_QUERY = "SELECT * FROM " 
			+ DAY_COUNTERS_TABLE 
			+ " WHERE " + DC_SHORT_URL_COLUMN + " = ?;";
	public static final String GET_HOUR_COUNTERS_QUERY = "SELECT * FROM " 
			+ HOUR_COUNTERS_TABLE 
			+ " WHERE " + HC_SHORT_URL_COLUMN + " = ? ;";
	public static final String GET_HOUR_COUNTERS_QUERY_LIMITED = "SELECT * FROM " 
			+ HOUR_COUNTERS_TABLE 
			+ " WHERE " + HC_SHORT_URL_COLUMN + " = ? AND " + HC_HOUR_COLUMN + " > ?;";
	public static final String GET_UNIQUE_COUNTER_QUERY = "SELECT * FROM " 
			+ UNIQUE_COUNTER_TABLE 
			+ " WHERE " + UC_SHORT_URL_COLUMN + " = ?;";
	public static final String GET_URL_QUERY = "SELECT * FROM " 
			+ URLS_TABLE + " WHERE " + URLS_SHORT_URL_COLUMN + " = ?;";
}
