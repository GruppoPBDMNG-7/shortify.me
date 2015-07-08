package me.shortify.dao;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;


public class CassandraDaoTest {
	public static void main(String[] args) {
		CassandraDAO d = new CassandraDAO();
		
		if (!d.checkUrl("p3n369")) {
			d.putUrl("p3n369", "www.google.it");
		}
		System.out.println(d.checkUrl("p3n369"));
		System.out.println(d.checkUrl("nonesisto"));
		
		System.out.println(d.getUrl("p3n369", "IT", "73.74.75.76", new GregorianCalendar()));
		
		Statistics s = d.getStatistics("p3n369");
		Object[] cDay = s.getDayCounters().keySet().toArray();
		Object[] cValue = s.getDayCounters().entrySet().toArray();
		
		for (int i = 0; i < cDay.length; i++) {
			Date dd = (Date) cDay[i];
			System.out.println(cValue[i]);
		}
		
		d.close();
	}
}
