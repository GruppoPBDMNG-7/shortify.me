package test.java;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import me.shortify.dao.CassandraDAO;
import me.shortify.dao.Statistics;
import junit.framework.TestCase;


public class CassandraDAOTest extends TestCase {

	private String[] checkUrlTest = {
			"provaSU!",//controlla url esistente
			"provasu!",//controlla case sensitive
			"NonEsiste!"//controlla short url non esistente
			};
	private boolean[] checkUrlResult = {true,false,false};
	
	CassandraDAO cd;
	protected void setUp() throws Exception {
		cd = new CassandraDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		cd.resetDB();
		super.tearDown();
	}

	public void testCheckUrl() {
		
		cd.putUrl("provaSU!","www.provaShortUrl.com");
		for(int i = 0; i < checkUrlTest.length ; i++){
		assertTrue("Il risultato e' "+cd.checkUrl(checkUrlTest[i]),cd.checkUrl(checkUrlTest[i]) == checkUrlResult[i]);
		}
	
	}

	public void testPutUrl() {
		//controlla che non esiste lo short url
		assertTrue("Il risultato e' "+cd.checkUrl("INESISTENTE!"),!cd.checkUrl("INESISTENTE!"));
		
		//controlla che esista lo short url che inserisco
		cd.putUrl("INESISTENTE!","www.provaShortUrl.com");			
		assertTrue("Il risultato e' "+cd.checkUrl("INESISTENTE!"),cd.checkUrl("INESISTENTE!"));	
	}

	public void testUpdateUrlStatistics() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		cd.putUrl("statsTestB","www.statsTest.com");
		cd.updateUrlStatistics("statsTestB", "IT", "127.0.0.1", c);
		
		Statistics st = cd.getStatistics("statsTestB", c, 0);		
		Object[] cDay = st.getDayCounters().keySet().toArray();		
		Object[] HDay = st.getHourCounters().keySet().toArray();	
		//Controlla se ha aggiornato il contatore country
		assertTrue("Il valore riscontrato e': "+ st.getCountryCounters().get("IT")+" invece che 1",
				st.getCountryCounters().get("IT") == 1);		
		//Controlla se ha aggiornato il contatore giornaliero di accessi
		assertTrue("Il valore riscontrato e': "+ st.getDayCounters().get(cDay[0])+" invece che 1",
				st.getDayCounters().get(cDay[0]) == 1);		
		//Controlla se ha aggiornato il contatore orario di accessi
		assertTrue("Il valore riscontrato e': "+ st.getHourCounters().get(HDay[0])+" invece che 1",
				st.getHourCounters().get(HDay[0]) == 1);
	}

	public void testGetStatistics() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		cd.putUrl("statsTestA","www.statsTest.com");
		cd.updateUrlStatistics("statsTestA", "IT", "127.0.0.1", c);
		
		Statistics st = cd.getStatistics("statsTestA", c, 0);		
		Object[] cDay = st.getDayCounters().keySet().toArray();		
		Object[] HDay = st.getHourCounters().keySet().toArray();	
		//Controlla se e' congruente il contatore country
		assertTrue("Il valore riscontrato e': "+ st.getCountryCounters().get("IT")+" invece che 1",
				st.getCountryCounters().get("IT") == 1);		
		//Controlla se e' congruente il contatore giornaliero di accessi
		assertTrue("Il valore riscontrato e': "+ st.getDayCounters().get(cDay[0])+" invece che 1",
				st.getDayCounters().get(cDay[0]) == 1);		
		//Controlla se e' congruente il contatore orario di accessi
		assertTrue("Il valore riscontrato e': "+ st.getHourCounters().get(HDay[0])+" invece che 1",
				st.getHourCounters().get(HDay[0]) == 1);
		//Controlla se e' congruente lo shortUrl
		assertTrue("Lo shortUrl e' "+st.getShortUrl()+" invece di statsTestA",
				st.getShortUrl().equals("statsTestA"));
		//Controlla se e' congruente il contatore di visite uniche
		assertTrue("Le visite uniche sono "+st.getUniqueViews()+" invece di 1",
				st.getUniqueViews() == 1);
		//Controlla se e' congruente il contatore di visite uniche dopo una visita ulteriore da parte
		//dello stesso ip.
		cd.updateUrlStatistics("statsTestB", "IT", "127.0.0.1", c);
		assertTrue("Le visite uniche sono "+st.getUniqueViews()+" invece di 1 con un accesso ulteriore dallo stesso ip",
				st.getUniqueViews() == 1);
	}

	public void testGetUrl() {
	
		//controlla se trova url inesistenti
		assertTrue("Ha trovato" +cd.getUrl("nonEsisto")+" invece che una stringa vuota",
				cd.getUrl("nonEsisto").equals(""));
		//controlla se trova url esistenti
		cd.putUrl("testGetUrl", "www.testGetUrl.com");
		assertTrue("Ha trovato" +cd.getUrl("testGetUrl")+" invece che www.testGetUrl.com ",
				cd.getUrl("testGetUrl").equals("www.testGetUrl.com"));
	}

}
