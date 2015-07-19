import me.shortify.utils.filter.WordChecker;
import junit.framework.TestCase;


public class WordCheckerTest extends TestCase {
	
	private String[] testWords = {
			"dick",	 		//English 	
			"kurva",		//Czech
			"pikslugeri",	//Danish
			"flamoes",		//Dutch
			"diofeka",		//Esperanto
			"hitosti",		//Finnish
			"clitoris",		//French
			"morgenlatte",	//German
			"fingokat",		//Hungarian
			"cazzo",		//Italian
			"kukene",		//Norwegian
			"burdelmama",	//Polish
			"cabrao",		//Portuguese
			"otpizdit",		//Russian
			"Cabron",		//Spanish
			"moonade",		//Swedish
			"orospuya",		//Turkish
			"pasta"			//Italian
			};
	private boolean[] testResult = {true,true,true,true,true,true,true,true,true,true,
			true,true,true,true,true,true,true,false};
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
		
		for(int i = 0; i < testWords.length; i++){
		assertTrue("Test bad word "+i+" failed",wc.isBadWord(testWords[i]) == testResult[i]);	
		}
		
	}

}
