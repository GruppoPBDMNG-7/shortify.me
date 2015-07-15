import me.shortify.utils.filter.DomainChecker;
import junit.framework.TestCase;


public class DomainCheckerTest extends TestCase {

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
		assertTrue("Test bad domain url 1:",dc.isBadDomain("testilla.ru"));
		assertTrue("Test bad domain url 2:",dc.isBadDomain("www.testilla.ru"));
		assertTrue("Test bad domain url 3:",dc.isBadDomain("http://testilla.ru"));
		assertTrue("Test bad domain url 4:",dc.isBadDomain("http://www.testilla.ru"));
		assertTrue("Test bad domain url 5:",dc.isBadDomain("https://testilla.ru"));
		assertTrue("Test bad domain url 6:",dc.isBadDomain("https://www.testilla.ru"));
	}

}
