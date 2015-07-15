import me.shortify.utils.filter.WordChecker;
import junit.framework.TestCase;


public class WordCheckerTest extends TestCase {

	WordChecker wc;
	protected void setUp() throws Exception {
		wc = new WordChecker();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		wc = null;
		super.tearDown();
	}

	public void testIsBadWord() {
		assertTrue("Test bad word in English (en):",wc.isBadWord("dick"));		
		assertTrue("Test bad word in Czech (cs):",wc.isBadWord("kurva"));
		assertTrue("Test bad word in Danish (da):",wc.isBadWord("pikslugeri"));
		assertTrue("Test bad word in Dutch (nl):",wc.isBadWord("flamoes"));
		assertTrue("Test bad word in Esperanto (eo):",wc.isBadWord("diofeka"));
		assertTrue("Test bad word in Finnish (fi):",wc.isBadWord("hitosti"));
		assertTrue("Test bad word in French (fr):",wc.isBadWord("clitoris"));
		assertTrue("Test bad word in German (de):",wc.isBadWord("morgenlatte"));
		assertTrue("Test bad word in Hungarian (hu):",wc.isBadWord("fingokat"));
		assertTrue("Test bad word in Italian (it):",wc.isBadWord("cazzo"));		
		assertTrue("Test bad word in Norwegian (no):",wc.isBadWord("kukene"));	
		assertTrue("Test bad word in Polish (pl):",wc.isBadWord("burdelmama"));
		assertTrue("Test bad word in Portuguese (pt):",wc.isBadWord("cabrao"));
		assertTrue("Test bad word in Russian (ru):",wc.isBadWord("otpizdit"));
		assertTrue("Test bad word in Spanish (es):",wc.isBadWord("Concha de tu madre"));
		assertTrue("Test bad word in Swedish (sv):",wc.isBadWord("moonade"));
		assertTrue("Test bad word in Turkish (tr):",wc.isBadWord("otuz bircilerden"));
		assertTrue("Test good word:",!wc.isBadWord("I love pasta"));	
	}

}
