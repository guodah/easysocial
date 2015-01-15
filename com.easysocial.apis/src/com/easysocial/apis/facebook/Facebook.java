package com.easysocial.apis.facebook;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.easysocial.apis.DBInterface;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.saver.sql.HsqlClient;

public class Facebook extends DBInterface{
	
	private Facebook(){
		
	}
	
	private static Facebook instance=null;
	
	public static Facebook newInstance(){
		if(instance==null){
			return new Facebook();
		}
		return instance;
	}
	
	/**
	 * Finds the Facebook id of the current user.
	 * @return
	 */
	public String getMe(){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(FacebookAction.GET_ME)); 
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			
			if(set.next()){
				
				return set.getString("id");
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;	
		}
		
	}
	
	/**
	 * Gets all member ids in your network on Facebook, including yourself.
	 * @return
	 */
	public String [] getAllMembers(){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(FacebookAction.GET_ALL_USER)); 
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			
			if(set.next()){
				ArrayList<String> ids = new ArrayList<String>();
				do{
					String id = set.getString("id");
					ids.add(id);
				}while(set.next());
				
				String resultsArray[] = new String[ids.size()];
				
				return (String[])ids.toArray(resultsArray);
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;	
		}
		
	}
	
	/**
	 * Searches for people's Facebook ids, given his/her first and last names.
	 * 
	 * The first name and last name in the parameter list are case insensitive.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return a list of matched people ids or null when search fails
	 */
	public String [] findPeople(String firstName, String lastName){
		if(!connected()){
			return null;
		}
		String sql = String.format(FacebookSqls.getSql(FacebookAction.FIND_PEOPLE_FULL_NAME),
				firstName, lastName); 
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			
			if(set.next()){
				ArrayList<String> ids = new ArrayList<String>();
				do{
					String id = set.getString("id");
					ids.add(id);
				}while(set.next());
				String resultsArray[] = new String[ids.size()];
				
				return (String[])ids.toArray(resultsArray);
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;	
		}
	}
	
	/**
	 * Searches for people's Facebook ids, given his/her first or last name.
	 * 
	 * The given name can match the real names partially.
	 * 
	 * @param name
	 * @return a list of matched people ids or null when search fails
	 */
	public String [] findPeople(String name){
		if(!connected()){
			return null;
		}
		String sql = String.format(FacebookSqls.getSql(FacebookAction.FIND_PEOPLE),
				'%'+name.toUpperCase()+'%','%'+name.toUpperCase()+'%'); 
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			
			if(set.next()){
				ArrayList<String> ids = new ArrayList<String>();
				do{
					String id = set.getString("id");
					ids.add(id);
				}while(set.next());
				String resultsArray[] = new String[ids.size()];
				
				return (String[])ids.toArray(resultsArray);
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;	
		}
	}
	
	/**
	 * Checks if two people are friends in Facebook.
	 * 
	 * @param id1
	 * @param id2
	 * @return
	 */
	public boolean friends(String id1, String id2) {
		if(!connected()){
			return false;
		}
		
		String sql = String.format(FacebookSqls.getSql(FacebookAction.FRIENDS),
				id1, id2);
		
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			return set.next();
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return false;				
		}
	}
	
	/**
	 * Finds the ID's of the friends, given a people's Facebook ID.
	 * 
	 * @param id
	 * @return a list of friend ids or null when search fails
	 */
	public String [] getFriends(String id){

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_FRIENDS),id);
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			while(set.next()){
				results.add(set.getString("id2"));
			}
			if(results.size()==0){
				return null;
			}
			String resultsArray[] = new String[results.size()];
			return (String[]) results.toArray(resultsArray);
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;			
		}

	}
	
	/**
	 * Gets the Facebook ids of the friends of the current user.
	 * 
	 * @return a list of friend ids or null when there are no friends
	 */
	public String[] getMyFriends(){
/*
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_MY_FRIENDS));
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			while(set.next()){
				results.add(set.getString("id1"));
			}
			if(results.size()==0){
				return null;
			}
			String resultsArray[] = new String[results.size()];
			return (String[]) results.toArray(resultsArray);
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;			
		}
*/
		if(!connected()){
			return null;
		}
		String[] allUsers = getAllMembers();
		if(allUsers==null){
			return null;
		}
		String me = getMe();
		if(me==null){
			return null;
		}
		Set<String> set = new HashSet<String>();
		for(String user:allUsers){
			set.add(user);
		}
		set.remove(me);
		String [] result = new String[set.size()];
		return set.toArray(result);

	}
	
	/**
	 * Finds the full name of a people given his/her Facebook ID.
	 * 
	 * @param id
	 * @return name of the people, null when search failed
	 */
	public String getName(String id){
		if(!connected()){
			return null;
		}


		String sql = String.format(FacebookSqls.getSql(FacebookAction.GET_NAME),id); 
		try{
			ResultSet set =client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			if(!set.next()){
				return null;
			}else{
				String name = set.getString("name");
				return name;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;			
		}
	}

	/**
	 * Given a Facebook id, returns a list of Facebook user id who have liked it.
	 * 
	 * Note some likers may not be your friends.
	 * 
	 * @param id
	 * @return list of liker or null if there are no likers
	 */
	public String[] getLikers(String id){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_LIKERS), id);
		try{
			ResultSet set =client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			if(set.next()){
				ArrayList<String> ids = new ArrayList<String>();
				do{
					String liker = set.getString("from_id");
					ids.add(liker);
				}while(set.next());
				String resultsArray[] = new String[ids.size()];
				
				return (String[])ids.toArray(resultsArray);
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}		
	}
	
	/**
	 * Finds the last name of a people given his/her Facebook ID.
	 * 
	 * @param id
	 * @return last name of the people, null when search failed
	 */
	public String getLastName(String id){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_LAST_NAME), id);
		try{
			ResultSet set =client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			if(!set.next()){
				return null;
			}else{
				String name = set.getString("last_name");
				return name;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}
	}
	
	/**
	 * Finds the first name of a people given his/her Facebook ID.
	 * 
	 * @param id
	 * @return name of the people, null when search failed
	 */
	public String getFirstName(String id){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(FacebookAction.GET_FIRST_NAME),id);
		try{
			ResultSet set =client.executeQuery( sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);

			if(!set.next()){
				return null;
			}else{
				String name = set.getString("first_name");
				return name;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}

	}
	
	/**
	 * Checks if a people is male or female given his/her Facebook ID.
	 * 
	 * @param id
	 * @return true if male, false otherwise
	 */
	public boolean male(String id){
		if(!connected()){
			return false;
		}

		
		String sql = String.format(	FacebookSqls.getSql(FacebookAction.IS_MALE), id);
		try{
			ResultSet set =client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			if(!set.next()){
				return false;
			}else{
				return set.getString("male").equals("male");
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return false;
		}
	}
	
	/**
	 * Given the ID of a status message, finds the ID of the author.
	 * 
	 * @param status_id
	 * @return the author Facebook id of the status or null when search fails
	 */
	public String getStatusAuthor(String status_id) {
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_STATUS_AUTHOR), status_id);
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			if(set.next()){
				String from_id = set.getString("from_id");
				return from_id;
			}
			return null;
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;			
		}
	}
	
	/**
	 * Searches for statuses whose content include a given phrase.
	 * 
	 * @param phrase
	 * @return a list of status ids or null when search fails
	 */
	public String [] findStatuses(String phrase){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.FIND_STATUSES_BY_MESSAGE),'%'+phrase.toUpperCase()+'%');
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			while(set.next()){
				results.add(set.getString("id"));
			}
			if(results.size()==0){
				return null;
			}
			String resultsArray[] = new String[results.size()];
			return (String[]) results.toArray(resultsArray);
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}
	}
	
	/**
	 * Given a people's Facebook ID, finds the ID's of his/her status messages
	 * 
	 * @param id
	 * @return a list of status ids or null when search fails.
	 */
	public  String[] getStatuses(String id){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_STATUS_IDS),id);
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			while(set.next()){
				results.add(set.getString("id"));
			}
			if(results.size()==0){
				return null;
			}
			String resultsArray[] = new String[results.size()];
			return (String[]) results.toArray(resultsArray);
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}
	}

	
	/**
	 * Finds the status message given an ID
	 * 
	 * @param id
	 * @return the textual content of the status or null when search fails.
	 */
	public String getStatusMessage(String id){
		if(!connected()){
			return null;
		}

		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_STATUS_MESSAGE),id);
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			if(set.next()){
				return set.getString("message");
			}else{
				return null;
			}
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;			
		}
	}
	
	/**
	 * Given the ID of a status message, finds the IDs of its comments.
	 * 
	 * @param status_id
	 * @return a list of comment ids of a given status id or null when search fails
	 */	
	public  String[] getComments(String status_id){
		if(!connected()){
			return null;
		}

		
		String sql = String.format(FacebookSqls.getSql(
				FacebookAction.GET_COMMENT), status_id);
		
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			ArrayList<String> results = new ArrayList<String>();
			while(set.next()){
				String from_id = set.getString("from_id");
				if(getName(from_id)!=null)
					results.add(set.getString("content"));
			}
			String resultsArray[] = new String[results.size()];
			return (String[]) results.toArray(resultsArray);
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return null;
		}
	}
	
	/**
	 * Given an ID, finds out if he/she is in the user's social network in Facebook.
	 * @param id
	 * @return
	 */
	public boolean inNetwork(String id){
		if(!connected()){
			return false;
		}

		String sql = String.format(FacebookSqls.getSql(FacebookAction.IN_NETWORK),id);
		try{
			ResultSet set = client.executeQuery(sql);
			EasySocialLogger.log(Level.INFO, "Submitted sql command: "+sql);
			return set.next();
		}catch(SQLException e){
			EasySocialLogger.log(Level.SEVERE, String.format("\'%s\' failed", sql), e);
			return false;			
		}
	}


}
