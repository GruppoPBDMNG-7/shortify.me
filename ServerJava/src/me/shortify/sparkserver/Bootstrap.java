package me.shortify.sparkserver;

import static spark.SparkBase.externalStaticFileLocation;


public class Bootstrap {
	
	public static void main( String[] args ) {   
		
		//folder del client web
		externalStaticFileLocation("/ClientAngular");
		
        Services.setupEndpoints();
	}
}
