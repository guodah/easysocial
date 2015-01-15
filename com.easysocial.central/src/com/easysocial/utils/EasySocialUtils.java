package com.easysocial.utils;

import java.sql.Connection;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

//import org.hsqldb.Server;





import com.easysocial.logging.EasySocialLogger;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.FacebookType;
import com.restfb.types.NamedFacebookType;

public class EasySocialUtils {
	
//	private static Connection db=null;
//	private static String access_token=null;
//	private static Server server=null;
	
	/**
	 * Puts the current thread sleep for a specified time.
	 * 
	 * @param milliseconds
	 */
	public static void sleep(long milliseconds){
		try{
			Thread.sleep(milliseconds);
		}catch(InterruptedException e){
			
		}
	}
	
	public static Map<String, String> parsePropertyList(String properties){
		Map<String, String> map = new HashMap<String, String>();
		if(properties==null){
			return map;
		}
		
		String equation = "(\\s*(([a-zA-Z][a-zA-Z0-9_]*)|([_][a-zA-Z0-9_]+))\\s*)=(\\s*([a-zA-Z0-9_]+)\\s*)";
		String [] segs = properties.split("\\s*,\\s*");
		for(String property:segs){
			if(property.matches(equation)){
				String [] nameValue = property.split("(\\s*=\\s*)|\\s+");
				if(nameValue[0].length()==0)
					map.put(nameValue[1], nameValue[2]);
				else
					map.put(nameValue[0], nameValue[1]);
			}
		}
		return map;
	}
	
}
