package java;
import java.io.IOException;
import java.net.URISyntaxException;

import junit.framework.TestCase;
import me.shortify.sparkserver.ShortenerServices;
import me.shortify.sparkserver.exception.BadCustomURLException;
import me.shortify.sparkserver.exception.BadURLException;
import me.shortify.sparkserver.exception.ShortURLNotFoundException;
import me.shortify.sparkserver.exception.URLExistsException;

import com.maxmind.geoip2.exception.GeoIp2Exception;


public class ShortenerServicesTest extends TestCase {

	ShortenerServices ss;
	
	protected void setUp() throws Exception {
		ss = new ShortenerServices(new FakeDAO());
		super.setUp();
	}

	protected void tearDown() throws Exception {
		ss = null;
		super.tearDown();
	}

	
	// --------- TEST PER METODO DI CONVERSIONE URL --------- //
	
	String casiTestConversioneURL[] = {
		"{longurl:prova}",
		"{\"longurl\":\"http://google.it\" , \"customText\":\"prova\"}",
		"{\"longurl\":\"http://url.it\"}",
		"{\"longurl\":\"http://google.it\" , \"customText\":\"p\"}",
		"{\"longurl\":\"http://google.it\" , \"customText\":\"\"}",
		"{\"longurl\":\"http://000007.ru\"}",
		"{\"longurl\":\"http://zzukoni.net\"}",
		"{\"longurl\":\"http://zzukoni.net\", \"customText\":\"femminuccia\"}",
		"{\"longurl\":\"\"}",
		"{\"longurl\":\"http://prova.it\", \"customText\":\"femminuccia\"}",
		"{\"longurl\":\"http://prova.it\", \"customText\":\"drittsekk\"}",
		"{\"longurl\":\"http://prova.it\", \"customText\":\"urlEsistente\"}",
		//aggiungi qui nuovi casi di test

	};

	String attesiConversioneURL[] = {
			"23",
			"{\"shortUrl\":\"prova\"}",
			"23",
			"{\"shortUrl\":\"p\"}",
			"23",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			//aggiungi qui nuovi valori attesi
	};
	
	public void testConversioneURL() {
		String valoreOttenuto = "";
		for(int i = 0; i < casiTestConversioneURL.length; i++) {
			try {
				valoreOttenuto = ss.conversioneURL(casiTestConversioneURL[i]);
			
				//casi di test pari non includono il custom url
				if (i % 2 == 0) {
					assertEquals(Integer.parseInt(attesiConversioneURL[i]), 
							valoreOttenuto.length() );
				} else {
					assertTrue(valoreOttenuto, valoreOttenuto.equals(attesiConversioneURL[i]));
				}
			} catch(BadURLException e) {
				if (i != 5 && i != 6 && i != 7 && i!= 8) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			} catch(BadCustomURLException e) {
				if (i != 9 && i != 10) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			} catch(URLExistsException e) {
				if (i != 11) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			}
		}
	}

	
	// --------- TEST PER METODO DI VISITA URL --------- //
	
	String casiTestVisitaURL[][] = {
			{"url1", "151.45.143.200"},
			{"url2", ""},
			{"url1", ""},
			//aggiungi qui nuovi casi di test
	};
	
	String attesiVisitaURL[] = {
			"long1",
			"",
			"long1",
			//aggiungi qui nuovi valori attesi
	};
	
	public void testVisitaURL() 
			throws IOException, URISyntaxException, GeoIp2Exception {
		String valoreOttenuto = "";
		for(int i = 0; i < casiTestVisitaURL.length; i++) {
			
			try {
			valoreOttenuto = ss.visitaURL(
					casiTestVisitaURL[i][0],
					casiTestVisitaURL[i][1]);
			
			assertTrue(valoreOttenuto.equals(attesiVisitaURL[i]));
			} catch (ShortURLNotFoundException e) {
				if (i != 1) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			}
		} 
			
	}
	
	// --------- TEST PER METODO DI ISPEZIONE URL --------- //
	
	String casiTestIspezionaURL[] = {
			"{shorturl:\"\"}",
			"{shorturl:urlEsistente}",
			"{shorturl:urlNonEsistente}",
			//aggiungi qui nuovi casi di test
			
	};
	
	String attesiIspezionaURL[] = {
			"",
			"{\"shortUrl\":\"urlEsistente\",\"hourCounters\":{},\"uniqueCounter\":0,\"longUrl\":\"long\",\"countryCounters\":{},\"dayCounters\":{}}",
			"",
			//aggiungi qui nuovi valori attesi
	};
	
	public void testIspezionaURL() {
		String valoreOttenuto = "";
		for(int i = 0; i < casiTestIspezionaURL.length; i++) {
			
			try {
				valoreOttenuto = ss.ispezionaURL(casiTestIspezionaURL[i]);
			
				assertTrue("CASO " + i 
						+ " ATTESO: " + attesiVisitaURL[i]
						+ " OTTENUTO: " + valoreOttenuto, 
						valoreOttenuto.equals(attesiIspezionaURL[i]));
			} catch (ShortURLNotFoundException e) {
				if (i != 2) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			} catch (BadURLException e) {
				if (i != 0) {
					fail("CASO DI TEST: " + i + " Eccezione non prevista!");
				}
			}
		} 
	}

}
