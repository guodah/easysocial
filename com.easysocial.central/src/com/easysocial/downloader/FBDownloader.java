package com.easysocial.downloader;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogHandler;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.networks.NodeAttribute;
import com.easysocial.networks.gephi.GephiNetwork;
import com.easysocial.networks.gephi.GephiNetworkBuilder;
import com.easysocial.oauthclient.OauthClient;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.oauthclient.fb.FBConstants;
import com.easysocial.oauthclient.fb.FBOauthClient;
import com.easysocial.packing.DataPacker;
import com.easysocial.saver.DataSaver;
import com.easysocial.saver.sql.HsqlClient;
import com.easysocial.saver.sql.SQLClient;
import com.easysocial.saver.sql.SQLDataSaver;
import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBLike;
import com.easysocial.types.downloaded.FBOwner;
import com.easysocial.types.downloaded.FBStatus;
import com.easysocial.types.downloaded.FBUser;
import com.easysocial.types.packed.SavableObject;
import com.restfb.Parameter;
import com.restfb.types.NamedFacebookType;

public class FBDownloader extends DataDownloader{
	
	private final static String NUM_FRIENDS = "num_friends";
	private final static String NUM_STATUSES = "num_statuses";
	
	public FBDownloader(OauthClient client, DataPacker packer, OauthParameters params) {
		super(client, packer, params);
	}
	
	public static FBDownloader newInstance(OauthParameters params, DataPacker packer){
		FBOauthClient client = FBOauthClient.newInstance(params);
		if(client==null){
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to find a correct client program for "
							+"downloading from Facebook");
			return null;
		}else{
			return new FBDownloader(client, packer, params);
		}
	}

	
	private static final int FRIEND_GROUP_SIZE = 100;
	
	/**
	 * Downloads friendship, likes and statuses of the user and his/her network.
	 */
	@Override
	public void download() {
		FBOauthClient client = (FBOauthClient) this.client;
		

		List<NamedFacebookType> allFriends = downloadNetwork(client);
		downloadData(client, allFriends);
		
		packer.close();
	}

	private void downloadData(FBOauthClient client,List<NamedFacebookType> allFriends) {
		int numOfStatuses = getNumStatuses();
		for(int start=0;start<allFriends.size();){
			int end = start+FRIEND_GROUP_SIZE>allFriends.size()?
					allFriends.size():start+FRIEND_GROUP_SIZE;
			List<NamedFacebookType> friends = allFriends.subList(
					start, end);

			List<FBLike> myLikes = client.getUserData(
					"me", "likes", FBLike.class,
					Parameter.with("limit", FBConstants.PAGE_LIMIT));
			pack(myLikes);
			myLikes = null;
			
			List<FBLike> friendsLikes = client.batchGetFriendConnectionsM(
					friends, "likes", FBLike.class, 
					Parameter.with("limit", FBConstants.PAGE_LIMIT));
			pack(friendsLikes);
			friendsLikes = null;
			
			List<FBStatus> friendsStatuses = client.batchGetFriendConnectionsM(
					friends, "statuses", FBStatus.class, 
					Parameter.with("limit", numOfStatuses));
			pack(friendsStatuses);
			friendsStatuses = null;
			
			List<FBStatus> myStatuses = client.getUserData("me", "statuses", FBStatus.class,
					Parameter.with("limit", numOfStatuses));
			pack(myStatuses);
			myStatuses = null;
			
			start = end;
		}		
	}

	private int getNumStatuses() {
		int num = 0;
		if(params.getLimitAccess()){
			try{
				num = Integer.parseInt(params.getIntegratorParam(NUM_STATUSES));
			}catch(Exception e){
				num = FBConstants.PAGE_LIMIT;
			}
		}else{
			num = FBConstants.PAGE_LIMIT;
		}
		return num;
	}

	private List<NamedFacebookType> downloadNetwork(FBOauthClient client) {
		List<NamedFacebookType> allFriends = client.getFriendsID();
		
		GephiNetworkBuilder networkBuilder = createGephiNetworkBuilder();
		addAllFriends(networkBuilder, allFriends);
		
		for(int start=0;start<allFriends.size();){
			int end = start+FRIEND_GROUP_SIZE>allFriends.size()?
					allFriends.size():start+FRIEND_GROUP_SIZE;
			List<NamedFacebookType> friends = allFriends.subList(
					start, end);
			
			List<FBOwner> friendships = client.batchGetFriendConnectionsM(friends, 
					"mutualfriends", FBOwner.class);
			
			addFriendships(networkBuilder, friendships);
			
			pack(friendships);
			friendships = null;
			
			FBUser me = client.readUserProfile("me");
			me.isMe(true);
			pack(Arrays.asList(me));
						
			List<FBUser> friendsProfiles = client.batchGetFriendConnectionsM(friends, "", 
					FBUser.class);
			pack(friendsProfiles);
			friendsProfiles = null;

			start = end;
		}
		
		EasySocialLogger.log(Level.INFO, "downloadNetwork calls filter");
		return filter(networkBuilder, allFriends);
		
	}

	private List<NamedFacebookType> filter(GephiNetworkBuilder networkBuilder, 
			List<NamedFacebookType> allFriends) {
		if(networkBuilder==null){
			return allFriends;
		}
		
		int num_friends = Integer.MAX_VALUE;
		
		try{
			EasySocialLogger.log(Level.INFO, "filter: NUM_FRIENDS: "+
					params.getIntegratorParam(NUM_FRIENDS));
			num_friends = Integer.parseInt(params.getIntegratorParam(NUM_FRIENDS));
		}catch(Exception e){
			EasySocialLogger.log(Level.SEVERE, "Integer.parseInt", e);
			return allFriends;
		}
		
		if(num_friends>=allFriends.size()){
			return allFriends;
		}
		
		GephiNetwork network = networkBuilder.build();
		network.analyze();
		HashMap<String, NamedFacebookType> friendsMap = new HashMap<String, NamedFacebookType>();
		for(NamedFacebookType friend:allFriends){
			friendsMap.put(friend.getId(), friend);
		}
		String [] ids = network.get(NodeAttribute.PAGERANK);
//		allFriends.clear();
		
		EasySocialLogger.log(Level.INFO, String.format(
				"Getting %d friends", num_friends));
		
		List<NamedFacebookType> result = new ArrayList<NamedFacebookType>();
		for(int i=0;i<num_friends;i++){
			result.add(friendsMap.get(network.getLabel(ids[i])));
		}
		return result;
	}

	private GephiNetworkBuilder createGephiNetworkBuilder() {
		if(params.getLimitAccess()){
			return new GephiNetworkBuilder();
		}else{
			return null;
		}
	}

	private void addFriendships(GephiNetworkBuilder networkBuilder,
			List<FBOwner> friendships) {
		if(networkBuilder==null){
			return;
		}
		for(FBOwner owner:friendships){
			networkBuilder.addEdge(owner.getOwner(), owner.getId());
		}
	}

	private void addAllFriends(GephiNetworkBuilder networkBuilder,
			List<NamedFacebookType> allFriends) {
		if(networkBuilder==null){
			return;
		}
		for(NamedFacebookType friend: allFriends){
			networkBuilder.addNode(friend.getId(), friend.getId());
		}
	}
	
}
