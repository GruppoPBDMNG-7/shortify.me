package me.shortify.utils.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blacklist {
	
	private List<String> languageList = new ArrayList<String>();
	private List<String> domainList   = new ArrayList<String>();
	
	public Blacklist(){
		
		File langList = new File(System.getProperty("user.dir")+
				"/src/me/shortify/utils/filter/languageList.txt");
		File domList = new File(System.getProperty("user.dir")+
				"/src/me/shortify/utils/filter/domainList.txt");
	
		BufferedReader rLang;
		BufferedReader rDomain;

		try {
			
			rLang = new BufferedReader(new FileReader(langList));
			rDomain = new BufferedReader(new FileReader(domList));
			
			String lang = null;
			while ((lang = rLang.readLine()) != null) {
				
		        languageList.add(lang);
		        }
			
			String dom = null;			
			while ((dom = rDomain.readLine()) != null) {
				
				domainList.add(dom);
		        }		
			
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}    
	}
	
	public boolean badWordsFinder(String word){
		boolean dirty = false;
		
		for(int i = 0; i < languageList.size(); i++){
			File file = new File(System.getProperty("user.dir")
					+"/src/me/shortify/utils/filter/languageFiles/"+languageList.get(i)+".txt");
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				String text = null;
				while ((text = reader.readLine()) != null) {
			        if(word.equalsIgnoreCase(text)){
			        	dirty = true;
			        	break;
			        }}
				}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} 
			if(dirty == true){
				break;
			}			
		}
		return dirty;
	}
	
	public boolean badDomainFinder(String longUrl){
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

	public static void main(String[] args) throws IOException {
		Blacklist bl = new Blacklist();		
		System.out.println(bl.badWordsFinder("dick"));
		System.out.println(bl.badDomainFinder("3b6.ru"));
	}

}
