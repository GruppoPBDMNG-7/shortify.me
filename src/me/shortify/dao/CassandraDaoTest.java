package me.shortify.dao;
import java.util.GregorianCalendar;


public class CassandraDaoTest {
	public static void main(String[] args) {
		CassandraDAO d = new CassandraDAO();
		
		if (!d.checkUrl("p3n369")) {
			d.putUrl("p3n369", "www.google.it");
		}
		System.out.println(d.checkUrl("p3n369"));
		System.out.println(d.checkUrl("nonesisto"));
		
		System.out.println(d.getUrl("p3n369", "IT", "73.74.75.76", new GregorianCalendar()));
		
		d.close();
	}
}
