import java.io.IOException;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import me.shortify.utils.geoLocation.CountryIPInformation;
import junit.framework.TestCase;


public class CountryIPInformationTest extends TestCase {

	CountryIPInformation cIPi;
	protected void setUp() throws Exception {
		cIPi = new CountryIPInformation();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		cIPi = null;
		super.tearDown();
	}

	public void testGetCountry() throws IOException, GeoIp2Exception {
			assertTrue("It isn't an American ip but "+ cIPi.getCountry("208.78.164.1")+ ":",
					cIPi.getCountry("208.78.164.1").equalsIgnoreCase("US"));
			
			assertTrue("It isn't a Sweden ip but "+ cIPi.getCountry("185.25.180.1")+ ":",
					cIPi.getCountry("185.25.180.1").equalsIgnoreCase("SE"));
			
			assertTrue("It isn't a Luxemburg ip but "+ cIPi.getCountry("146.66.158.1")+ ":",
					cIPi.getCountry("146.66.158.1").equalsIgnoreCase("LU"));
			
			assertTrue("It isn't a German ip but "+ cIPi.getCountry("5.101.162.152")+ ":",
					cIPi.getCountry("5.101.162.152").equalsIgnoreCase("DE"));
			
			assertTrue("It isn't a Great Britain ip but "+ cIPi.getCountry("37.220.25.157")+ ":",
					cIPi.getCountry("37.220.25.157").equalsIgnoreCase("GB"));
			
			assertTrue("It isn't an Italian ip but "+ cIPi.getCountry("151.100.72.99")+ ":",
					cIPi.getCountry("151.100.72.99").equalsIgnoreCase("IT"));
	}

}
