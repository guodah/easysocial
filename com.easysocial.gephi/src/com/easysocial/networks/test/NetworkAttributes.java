package com.easysocial.networks.test;

import java.util.HashMap;

import java.util.Map;
//import com.easysocial.apis.EasySocial;
import com.easysocial.networks.CommunityType;
import com.easysocial.networks.NodeAttribute;
import com.easysocial.networks.gephi.GephiNetwork;

public class NetworkAttributes {
	/*
	public static GephiNetwork getFriendshipNetwork(){
		String [] friends = EasySocial.facebook.getMyFriends();

		Map<String, Integer> allPeople = new HashMap<String, Integer>();
		for(int i=0;i<friends.length; 
				allPeople.put(friends[i], i),i++);
		
		boolean [][] friendships = new boolean[allPeople.size()][allPeople.size()];
		String []names = new String[allPeople.size()];
		for(int i=0;i<friends.length;i++){
			String friend = friends[i];
			names[i] = EasySocial.facebook.getName(friend);
			String[] list = EasySocial.facebook.getFriends(friend);
			if(list==null){
				continue;
			}
			for(int j=0;j<list.length;j++){
				Integer index1 = allPeople.get(friend);
				Integer index2 = allPeople.get(list[j]);
				if(index2==null){
					continue;
				}
				friendships[index1][index2] = true;
			}
		}
		
		GephiNetwork network = GephiNetwork.newInstance(names, friendships);
		network.analyze();
		return network;
	}
	
	public static void main(String args[]){
		GephiNetwork friendship = getFriendshipNetwork();
		
		// sorts the network into communities where members within a community
		// are closer related than to members outside the community
		String [][] communities = friendship.get(CommunityType.MODULARITY);
		
		// sorts the network members using their page rank scores
		// the first member in the array has the highest page rank score
		String pageRankList[] = friendship.get(NodeAttribute.PAGERANK);
		
		// for each community, finds the member with the highest page rank score
		for(String[] community:communities){
			int highest = Integer.MAX_VALUE;
			String highestId = null;
			for(String member:community){
				for(int i=0;i<pageRankList.length;i++){
					if(pageRankList[i].equals(member)){
						if(highest>i){
							highest=i;
							highestId=member;
						}
						break;
					}
				}
			}
			System.out.printf("In this community of %d members, "+
					"%s has the highest PageRank score\n", community.length,
					friendship.getLabel(highestId));
		}

	}
	*/
}
