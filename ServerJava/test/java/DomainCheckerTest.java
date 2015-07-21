package java;
import me.shortify.utils.filter.DomainChecker;
import junit.framework.TestCase;


public class DomainCheckerTest extends TestCase {
	
	private String[] testUrls = {"testilla.ru","www.testilla.ru",
			"http://testilla.ru","http://www.testilla.ru",
			"https://testilla.ru","https://www.testilla.ru","www.google.com"};
	
	private boolean[] testResult = {true,true,true,true,true,true,false};
	
	DomainChecker dc;
	protected void setUp() throws Exception {
		dc = new DomainChecker();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		dc = null;
		super.tearDown();
	}

	public void testIsBadDomain() {
		
		for(int i = 0; i<testUrls.length; i++){
		assertTrue("Test failed bad domain url "+ i +":",dc.isBadDomain(testUrls[i]) == testResult[i]);
		}
	}

}
