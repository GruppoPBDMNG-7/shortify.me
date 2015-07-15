import me.shortify.utils.shortenerUrl.Algorithm;
import junit.framework.TestCase;


public class AlgorithmTest extends TestCase {
	
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
		assertTrue("ERRORE HASH DIVERSI",su.buildShortUrl("www").equals(su.buildShortUrl("www")));
		//verifico che la stessa stringa con lettere diverse restituisca hashing diversi.
		assertTrue("ERRORE HASH UGUALI",!su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/").equals(su.buildShortUrl("https://www.reddit.com/r/GlobalOffensive/")));
		
	}

}
