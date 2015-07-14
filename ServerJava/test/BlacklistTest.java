import me.shortify.utils.filter.Blacklist;
import junit.framework.TestCase;


public class BlacklistTest extends TestCase {

	Blacklist bl;
	protected void setUp() throws Exception {
		bl = new Blacklist();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		bl = null;
		super.tearDown();
	}

	public void testBadWordsFinder() {
		
		assertTrue("Test bad word in English (en):",bl.badWordsFinder("dick"));		
		assertTrue("Test bad word in Czech (cs):",bl.badWordsFinder("kurva"));
		assertTrue("Test bad word in Danish (da):",bl.badWordsFinder("pikslugeri"));
		assertTrue("Test bad word in Dutch (nl):",bl.badWordsFinder("flamoes"));
		assertTrue("Test bad word in Esperanto (eo):",bl.badWordsFinder("diofeka"));
		assertTrue("Test bad word in Finnish (fi):",bl.badWordsFinder("hitosti"));
		assertTrue("Test bad word in French (fr):",bl.badWordsFinder("clitoris"));
		assertTrue("Test bad word in German (de):",bl.badWordsFinder("morgenlatte"));
		assertTrue("Test bad word in Hungarian (hu):",bl.badWordsFinder("fingokat"));
		assertTrue("Test bad word in Italian (it):",bl.badWordsFinder("cazzo"));		
		assertTrue("Test bad word in Norwegian (no):",bl.badWordsFinder("kukene"));	
		assertTrue("Test bad word in Polish (pl):",bl.badWordsFinder("burdelmama"));
		assertTrue("Test bad word in Portuguese (pt):",bl.badWordsFinder("cabrao"));
		assertTrue("Test bad word in Russian (ru):",bl.badWordsFinder("otpizdit"));
		assertTrue("Test bad word in Spanish (es):",bl.badWordsFinder("Concha de tu madre"));
		assertTrue("Test bad word in Swedish (sv):",bl.badWordsFinder("moonade"));
		assertTrue("Test bad word in Turkish (tr):",bl.badWordsFinder("otuz bircilerden"));
		assertTrue("Test good word:",!bl.badWordsFinder("I love pasta"));		
		
		
		
	}

	public void testBadDomainFinder() {
	
		assertTrue("Test bad domain url 1:",bl.badDomainFinder("testilla.ru"));
		assertTrue("Test bad domain url 2:",bl.badDomainFinder("www.testilla.ru"));
		assertTrue("Test bad domain url 3:",bl.badDomainFinder("http://testilla.ru"));
		assertTrue("Test bad domain url 4:",bl.badDomainFinder("http://www.testilla.ru"));
		assertTrue("Test bad domain url 5:",bl.badDomainFinder("https://testilla.ru"));
		assertTrue("Test bad domain url 6:",bl.badDomainFinder("https://www.testilla.ru"));
	}

}
