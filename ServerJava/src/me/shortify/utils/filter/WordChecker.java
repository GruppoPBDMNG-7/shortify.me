package me.shortify.utils.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordChecker {
	private List<String> languageList = new ArrayList<String>();
	
	public WordChecker(){
		File langList = new File(System.getProperty("user.dir")+
				"/src/me/shortify/utils/filter/languageList.txt");		
	
		BufferedReader rLang;	

		try {			
			rLang = new BufferedReader(new FileReader(langList));			
			String lang = null;
			while ((lang = rLang.readLine()) != null) {
				
		        languageList.add(lang);
		        }		
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}    
	}
	
	public boolean isBadWord(String word){
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
	
}
