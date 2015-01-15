package com.easysocial.logging;

import java.util.logging.Level;

public class ConsoleLogger implements EasySocialLogHandler{

	private static ConsoleLogger logger;
	
	public static ConsoleLogger newInstance(){
		if(logger!=null){
			return logger;
		}else{
			logger = new ConsoleLogger();
			return logger; 
		}
	}
	
	private ConsoleLogger(){
		
	}
	
	@Override
	public void log(Level level, String msg) {
		System.out.printf("%s: %s\n", level.getName(),msg);
	}

	@Override
	public void log(Level level, String msg, Throwable thrown) {
		log(level, msg);
		if(thrown!=null)
			thrown.printStackTrace();
	}

}
