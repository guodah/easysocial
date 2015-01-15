package com.easysocial.apis.facebook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogger;

/**
 * This class checks out the SQL templates itemized in EasySocialAction.
 * 
 * @author Dahai Guo
 *
 */
class FacebookSqls {
	
	private static Map<String, String> map = null;
	
	private static void init(String path) throws FileNotFoundException, IOException{
		if(map!=null){
			return;
		}
		
		Properties properties = new Properties();
		properties.load(FacebookSqls.class.getResourceAsStream(path));
		
		map = new HashMap<String, String>();
		Set<Object> keys = properties.keySet();
		for(Object key:keys){
			String value = properties.getProperty((String) key);
			map.put((String) key, value);
		}
	}
	
	public static String getSql(FacebookAction action){
		if(map==null){
			try {
				init("Facebook_sql.properties");
				EasySocialLogger.log(Level.INFO, "Facebook SQL templates loaded");
			} catch (IOException e) {
				EasySocialLogger.log(Level.SEVERE, 
						String.format("Failed to load SQL templates from %s", 
								"Facebook_sql.properties"),e);
			}
		}
		return map.get(action.getActionCode());
	}
}
