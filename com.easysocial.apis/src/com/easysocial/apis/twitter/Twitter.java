package com.easysocial.apis.twitter;

import com.easysocial.apis.DBInterface;


import com.easysocial.apis.facebook.Facebook;

/**
 * Nothing up to now
 * 
 * @author Dahai Guo
 *
 */
public class Twitter extends DBInterface{
	private Twitter(){
		
	}
	
	private static Twitter instance=null;
	
	public static Twitter newInstance(){
		if(instance==null){
			return new Twitter();
		}
		return instance;
	}

}
