package me.shortify.dao;
import java.util.Date;
import java.util.GregorianCalendar;


public class CassandraDaoTest {
	public static void main(String[] args) {
		CassandraDAO d = new CassandraDAO();
		
		if (!d.checkUrl("abc123")) {
			d.putUrl("abc123", "www.google.it");
		}
		System.out.println(d.checkUrl("abc123"));
		System.out.println(d.checkUrl("nonesisto"));
		
		System.out.println(d.getUrl("abc123"));
		d.updateUrlStatistics("abc123", "IT", "73.74.75.76", new GregorianCalendar());
		
		Statistics s = d.getStatistics("abc123");
		Object[] cDay = s.getDayCounters().keySet().toArray();
		Object[] cValue = s.getDayCounters().entrySet().toArray();
		
		for (int i = 0; i < cDay.length; i++) {
			Date dd = (Date) cDay[i];
			System.out.println(cValue[i]);
		}
		
		System.out.println(s.toJson());
		
		d.close();
	}
}
