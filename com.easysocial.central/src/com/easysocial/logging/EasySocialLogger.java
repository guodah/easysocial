package com.easysocial.logging;

import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;

public class EasySocialLogger {
	
	private static List<EasySocialLogHandler> handlers;
	
	private static void init(){
		handlers = new ArrayList<EasySocialLogHandler>();
	}
	
	public static void addLogHandler(EasySocialLogHandler handler){
		if(handlers==null){
			init();
		}
		handlers.add(handler);
	}
	
	public static void removeLogHandler(EasySocialLogHandler handler){
		if(handlers==null)
			return;
		handlers.remove(handler);
	}
	
	public static void log(Level level, String msg, Throwable thrown){
		if(handlers==null){
			return;
		}
		
		for(EasySocialLogHandler handler:handlers){
			handler.log(level, msg, thrown);
		}
	}
	
	public static void log(Level level, String msg){
		log(level, msg, null);
	}
	
	public static void enableConsoleLogging(){
		addLogHandler(ConsoleLogger.newInstance());
	}
	
	public static void disableConsoleLogger(){
		handlers.remove(ConsoleLogger.newInstance());
	}
}
