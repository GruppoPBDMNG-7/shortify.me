package me.shortify.utils.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomainChecker {
	
	private List<String> domainList   = new ArrayList<String>();
	
	public DomainChecker(){
	File domList = new File(System.getProperty("user.dir")+
			"/src/me/shortify/utils/filter/domainList.txt");


	BufferedReader rDomain;

	try {	
		
		rDomain = new BufferedReader(new FileReader(domList));		
		String dom = null;			
		while ((dom = rDomain.readLine()) != null) {
			
			domainList.add(dom);
	        }		
		
		}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}    
	}
	
	public boolean isBadDomain(String longUrl){
		boolean dirty = false;
		for(int i = 0; i < domainList.size(); i++){
			
				if(longUrl.contains(domainList.get(i))){
					dirty = true;
				}
			if(dirty == true){
				break;
			}
			
		}
		
		return dirty;
	}	
}
