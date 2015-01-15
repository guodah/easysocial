package com.easysocial.apis;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.easysocial.apis.facebook.Facebook;
import com.easysocial.apis.twitter.Twitter;
import com.easysocial.logging.EasySocialLogger;

/**
 * Contains two static variables to access user's data in Facebook and Twitter.
 * 
 * The data are supposed to have been downloaded prior to use.
 *  
 * @author Dahai Guo
 *
 */
public class EasySocial {
	public final static Facebook facebook = Facebook.newInstance();
	public final static Twitter twitter = Twitter.newInstance();
	private static boolean started = start();

	private static boolean start(){
//		EasySocialLogger.enableConsoleLogging();
		
		Properties properties=new Properties();
		try {
			properties.load(new FileInputStream(new File("easysocial/db.properties")));
			String _dbs = (String) properties.get("db_names");
			EasySocialLogger.log(Level.INFO, "db_names :"+_dbs);
			if(_dbs==null){
				throw new IllegalStateException("db.properties does not contain db_names");
			}
			String [] dbs = _dbs.split(",");
			for(String db:dbs){
				String path = db.substring(0, db.lastIndexOf('/'));
				String name = db.substring(db.lastIndexOf('/')+1, db.length());
				
				if(name.equals("Facebook")){
					EasySocialLogger.log(Level.INFO, "Facebook db exists");
					facebook.init(path, name);
				}else if(name.equals("Twitter")){
					EasySocialLogger.log(Level.INFO, "Twitter db exists");
					twitter.init(path, name);
				}
			}
			return true;
		} catch (IOException e) {
			EasySocialLogger.log(Level.SEVERE, "Failed to load db.properties", e);
			return false;
		} catch (IllegalStateException e){
			EasySocialLogger.log(Level.SEVERE, e.toString(), e);
			return false;
		}

	}
}
