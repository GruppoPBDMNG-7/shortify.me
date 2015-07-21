package java;
import java.io.IOException;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import me.shortify.utils.geoLocation.CountryIPInformation;
import junit.framework.TestCase;


public class CountryIPInformationTest extends TestCase {

	private String[] listIp = {"208.78.164.1","185.25.180.1","146.66.158.1"
			,"5.101.162.152","37.220.25.157","151.100.72.99"};
	
	private String[] listCountry ={"US","SE","LU","DE","GB","IT"};
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
		
		for(int i = 0; i< listIp.length ;i++){
			
			assertTrue("It isn't an "+listCountry[i]+" ip but "+ cIPi.getCountry(listIp[i])+ ":",
					cIPi.getCountry(listIp[i]).equalsIgnoreCase(listCountry[i]));
		}
	}

}
