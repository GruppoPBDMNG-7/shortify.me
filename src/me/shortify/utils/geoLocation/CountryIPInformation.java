package me.shortify.utils.geoLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.*;

public class CountryIPInformation {

		private DatabaseReader reader;
		private File database ;
		private InetAddress ipAddress;
		
		/**
		 * Carico il file contenente il database con i range dei vari ip per nazione
		 * inizializzo il reader per utilizzare il database
		 * @throws IOException
		 * @throws URISyntaxException
		 */
		public CountryIPInformation() throws IOException, URISyntaxException{ 
			//TODO Verificare se il path rimane costantemente corretto in questa formula cambiando 
			//ambiente di lavoro
			database = new File(System.getProperty("user.dir")+"/src/me/shortify/utils/geoLocation/GeoLite2-Country.mmdb");			
			reader = new DatabaseReader.Builder(database).build();
		}
		
		/**
		 * metto nel parametro ip, un indirizzo ip
		 * @param ip
		 * @throws IOException
		 */
		public void setDataIP(String ip) throws IOException{
			
			  ipAddress = InetAddress.getByName(ip.toLowerCase());
		}
		
		public String getCountry() throws IOException, GeoIp2Exception{
			
			CountryResponse response = reader.country(ipAddress);
			Country country = response.getCountry();	
			return country.getIsoCode();
		}

	  public static void main(String[] args) throws IOException, GeoIp2Exception, URISyntaxException {
		
		  CountryIPInformation cii = new CountryIPInformation();
		  cii.setDataIP("80.181.140.244");	  
		  System.out.println(cii.getCountry());	
	
	  }
	

}
