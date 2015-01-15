package com.easysocial.networks.test;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import com.easysocial.apis.EasySocial;
import com.easysocial.networks.Network;
import com.easysocial.networks.gephi.GephiNetwork;

public class StatusLiker {
	/*
	public static void main(String args[]){
		// get all your friends and yourself
		String [] members = EasySocial.facebook.getAllMembers();
		
		// for each member, likerMap maintains a set of likers.
		// each liker has at least liked one of this member's status
		Map<String, Set<String>> likerMap = new HashMap<String, Set<String>>();
		
		// each time, a new liker or new liked member is found, they
		// will be added to this map. The String key is the id and the Integer
		// value records the order of being discovered.
		Map<String,Integer> indices = new HashMap<String, Integer>();
		int index = 0;
		
		for(String friend:members){
			String status_ids [] = EasySocial.facebook.getStatuses(friend);
			if(status_ids==null){
				continue;
			}
			for(String status_id: status_ids){
				String likers[] = EasySocial.facebook.getLikers(status_id);
				if(likers==null){
					continue;
				}

				for(String liker:likers){
					// note some likers may not be in your network
					if(EasySocial.facebook.inNetwork(liker)){
						if(!likerMap.containsKey(friend)){
							likerMap.put(friend, new HashSet<String>());
						}
						
						likerMap.get(friend).add(liker);
						
						if(!indices.containsKey(friend)){
							indices.put(friend, index++);
						}
						
						if(!indices.containsKey(liker)){
							indices.put(liker, index++);
						}
					}
				}
			}
		}
		
		// builds the array of member names
		String names[] = new String[indices.size()];
		for(String people: indices.keySet()){
			names[indices.get(people)] = EasySocial.facebook.getName(people);
		}
		
		// if liking[i][j] is true, names[i] has liked at least one status of names[j]
		boolean liking[][] = new boolean[names.length][names.length];
		for(String people: likerMap.keySet()){
			int index1 = indices.get(people);
			for(String liker:likerMap.get(people)){
				int index2 = indices.get(liker);
				liking[index2][index1] = true;
			}
		}
		
		// builds a gephi network to take advantage of gephi toolkit
		Network network = GephiNetwork.newInstance(names, liking);
		network.layout();
		network.rank();
		network.partition();
		network.visualize();
		
	}
	*/
}
