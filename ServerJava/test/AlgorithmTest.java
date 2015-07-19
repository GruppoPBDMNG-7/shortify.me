import me.shortify.utils.shortenerUrl.Algorithm;
import junit.framework.TestCase;


public class AlgorithmTest extends TestCase {
	
	private final int maxSULength = 8;
	Algorithm su;
	protected void setUp() throws Exception {
		su = new Algorithm();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		su = null;
		super.tearDown();
	}

	public void testBuildShortUrl() {
		//verifico che la stessa stringa con lettere uguali restituisca hashing uguali.
		assertTrue("1 ERRORE HASH DIVERSI",su.buildShortUrl("www").equals(su.buildShortUrl("www")));
		
		//verifico che la stessa stringa con lettere diverse restituisca hashing diversi.
		assertTrue("2 ERRORE HASH UGUALI",!su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/").equals(su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/")));
	
		//verifico che la stringa ha 8 caratteri con un url che ha più di 8 caratteri
		assertTrue("3 ERRORE HASH DI LUNGHEZZA "+su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/").length(),
				su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/").length() == maxSULength);
		
		//verifico che la stringa ha 8 caratteri con un url che ha meno di 8 caratteri
		assertTrue("4 ERRORE HASH DI LUNGHEZZA "+su.buildShortUrl("short.io").length() ,su.buildShortUrl("short.io").length() == maxSULength);
	}

}
