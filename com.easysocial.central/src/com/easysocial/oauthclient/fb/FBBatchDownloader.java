package com.easysocial.oauthclient.fb;

import java.util.ArrayList;


import com.restfb.DefaultJsonMapper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import com.easysocial.types.downloaded.FBOwner;
import com.easysocial.utils.EasySocialUtils;
import com.easysocial.logging.EasySocialLogger;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchResponse;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.User;

/**
 * This class is for multithreaded downloading data for an EasySocial project.
 * 
 * @author Dahai Guo
 *
 * @param <T> the data type for receiving downloaded data.
 */
public class FBBatchDownloader<T> implements Runnable{
	private List<NamedFacebookType> friends;
	private FacebookClient client;
	private String typeName;
	private Class<T> type;
	private List<T> data;
	private Semaphore sem;
	private Parameter[] parameters;
	
	private final static int MAX_TRIES = 5;
	
	/**
	 * Creates a downloader.
	 * 
	 * When the friends is null, this downloader will download the data for "me". When
	 * typeName is an empty string, the user's friends' ids will be downloaded.
	 * 
	 * @param client facebook client
	 * @param friends a list of friends
	 * @param typeName which type of connection is being requested
	 * @param type the data structure for receiving downloaded data
	 * @param sem the semaphore which will raised once this downloading
	 * 				job completes
	 * @param parameters download parameters
	 */
	public FBBatchDownloader(FacebookClient client, List<NamedFacebookType> friends, 
			String typeName, Class<T> type, Semaphore sem, Parameter...parameters){
		
		if(friends.size()> FBConstants.BATCH_REQUEST_LIMIT){
			throw new IllegalStateException	(String.format(
					"Facebook only allows at most %d requests in a batch",
					FBConstants.BATCH_REQUEST_LIMIT));
		}
		
		this.friends = friends;
		this.client = client;
		this.typeName = typeName;
		this.type = (Class<T>) ((type!=null)?type:User.class);
		this.sem = sem;
		data = new ArrayList<T>();
		this.parameters = parameters; 
	}
	
	@Override
	public void run() {
		EasySocialUtils.sleep((new Random()).nextInt(1000));
    	
    	List<BatchRequest> reqs = new ArrayList<BatchRequest>();
    	for(int i=0;i<friends.size();i++){
    		BatchRequest req = new BatchRequest.
    				BatchRequestBuilder(friends.get(i).getId()+
    						"/"+typeName).parameters(parameters).build();
    		reqs.add(req);
    	}
    	
    	BatchRequest[] reqArray = new BatchRequest[reqs.size()];
    	reqArray = reqs.toArray(reqArray);
    	
    	int numOfTries=0;
    	        	
    	// executing the requests in the batch
    	List<BatchResponse> batchResponses;
    	while(true){
    		try{
    			batchResponses = client.executeBatch(reqArray);
    	    	for(int j=0;j<batchResponses.size();j++){
    	    		
    	    		if(typeName.equals("")){
    	    			// fetch objects
    	    			T obj = (new DefaultJsonMapper()).toJavaObject(
    	    					batchResponses.get(j).getBody(), type);
    	    			this.data.add(obj);
    	    		}else{
    	    			// fetch connections
	    	    		Connection<T> _cons = new Connection<T>(client, 
	    						batchResponses.get(j).getBody(), type);
	    	    		
	    	    		String friendId = friends.get(j).getId();
	    	    		List<T>cons = _cons.getData();
	    	    		setFrom(cons, friendId);
	    	    		this.data.addAll(cons);
	    	    		
	    	   // 		if(typeName.equals("statuses")){
		    	//    		EasySocialLogger.log(Level.INFO, 
		    	 //   				friendId+" got "+cons.size()+" "+typeName);
	    	    //		}
    	    		}
    	    	}
    			break;
    		}catch(RuntimeException e){
    			if(++numOfTries==MAX_TRIES){
    				EasySocialLogger.log(Level.SEVERE, 
       					"Exception occurs when downloading \'"+
   	    					typeName +"\' data. Quitting...",e);
    				String ids="";
    				for(NamedFacebookType friend:friends){
    					ids+=friend.getId()+",";
    				}
    				EasySocialLogger.log(Level.SEVERE,
    						"Failed to download "+typeName+"\' data for "+ids);
    				break;
    			}
    			EasySocialLogger.log(Level.WARNING, 
    					"Exception occurs when downloading \'"+
    					typeName +"\' data. Trying again...",e);
    			EasySocialUtils.sleep(1000);
    		}		
    	}
    	sem.release();
	}
	
	private void setFrom(List<?> data, String from){
		if(data==null || data.size()==0 ||
				!(data.get(0) instanceof FBOwner)){
			return;
		}else{
			for(int k=0;k<data.size();k++){
				((FBOwner)data.get(k)).setOwner(from);
			}
		}
	}
	
	public List<T> getData(){
		return data;
	}
}
