package me.shortify.utils.shortenerUrl;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

public class Algorithm {
	
	private final int HASHLENGTH = 8;	
	
	public String buildShortUrl(String url){
		
		String strUrl = url;
		Random rnd = new Random();	
		char[] arrayChar = strUrl.toCharArray();
		char[] moddedChar = new char[HASHLENGTH];
			
		
		for(int i = 0; i<HASHLENGTH; i++){
			moddedChar[i] = arrayChar[rnd.nextInt(arrayChar.length-1)]; 
		}
		//char->string
		String newString = String.valueOf(moddedChar);
		byte[] encodedBytes = Base64.encodeBase64(newString.getBytes());
		String str = "";
		try {
			str = new String(encodedBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				
		//System.out.println(str.substring(0,HASHLENGTH));
		
		return str.substring(0,HASHLENGTH);
		
	}
	

}
