package com.easysocial.oauthclient.fb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import com.easysocial.oauthclient.fb.FBBatchDownloader;
import com.easysocial.oauthclient.fb.FBConstants;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthClient;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBUser;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.User;

public class FBOauthClient extends OauthClient{
	public String access_token;
	private FBOauthClient(OauthParameters params) {
		super(params);
		access_token = params.getTokens().get("access_token");
	}

	public <T> List<T> batchGetFriendConnectionsM(List<NamedFacebookType> friends, 
			String typeName, Class<T> type, Parameter...parameters){
		List<T> data = new ArrayList<T>();
		int numOfBatches = friends.size()/FBConstants.BATCH_REQUEST_LIMIT 
				+ (friends.size()%FBConstants.BATCH_REQUEST_LIMIT==0?0:1);
		Semaphore sem = new Semaphore(1-numOfBatches);
		Runnable rs[] = new Runnable[numOfBatches];

		EasySocialLogger.log(Level.INFO, String.format(
				"Dispatching %d threads for downloading \'%s\' data",
				numOfBatches, typeName
				));
		
		for(int i=0;i<numOfBatches;i++){
			int start = i*FBConstants.BATCH_REQUEST_LIMIT;
			int end = (i==numOfBatches-1)?friends.size():start+
					FBConstants.BATCH_REQUEST_LIMIT;
			
			List<NamedFacebookType> sub = friends.subList(start,  end);
			
			rs[i] = new FBBatchDownloader<T>(new DefaultFacebookClient(access_token), 
					sub, typeName, type, sem, parameters);
			Thread t = new Thread(rs[i]);
			t.start();
		}
		try{
			sem.acquire();
		}catch(InterruptedException e){
			return null;
		}
		for(int i=0;i<numOfBatches;i++){
			data.addAll(((FBBatchDownloader)rs[i]).getData());
		}

		EasySocialLogger.log(Level.INFO, String.format(
				"\'%s\' data downloaded",typeName));
		
		return data;

	}
	
	public static FBOauthClient newInstance(OauthParameters params){
		if(params.getTokens()!=null && params.getTokens().containsKey("access_token")){
			return new FBOauthClient(params);
		}else{
			EasySocialLogger.log(Level.SEVERE, 
					"Incorrect parameters provided.");
			return null;
		}
	}

	public List<NamedFacebookType> getFriendsID(){
		FacebookClient fbClient = getFBClient();
		EasySocialLogger.log(Level.INFO, "Downloading fb friend ids");
		Connection<NamedFacebookType> myFriends = 
				fbClient.fetchConnection("me/friends", NamedFacebookType.class);
		EasySocialLogger.log(Level.INFO, "Downloaded fb friend ids");
		return myFriends.getData();
	}
	
	public FBUser readUserProfile(String user){
		FacebookClient fbClient = getFBClient();
		EasySocialLogger.log(Level.INFO, "Downloading fb user profile");
		FBUser userProfile = fbClient.fetchObject(user+"/", FBUser.class);
		EasySocialLogger.log(Level.INFO, "Downloaded fb user profile");
		return userProfile;
	}
	
	public <T extends DownloadedObject> List<T> getUserData(String user, String connection, 
			Class<T> cl, Parameter...parameters){
		FacebookClient fbClient = getFBClient();
		EasySocialLogger.log(Level.INFO, "Downloading fb user's "+connection+" data.");
		List<T> data = 
				fbClient.fetchConnection(user+"/"+connection, cl, parameters).getData();
		EasySocialLogger.log(Level.INFO, "Downloaded fb user's "+connection+" data.");
		return data;
	}
	
	private FacebookClient getFBClient(){
		FacebookClient fbClient = new DefaultFacebookClient(access_token);
		return fbClient;
	}
}
