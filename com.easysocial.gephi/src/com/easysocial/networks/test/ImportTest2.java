package com.easysocial.networks.test;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.easysocial.networks.CommunityType;
import com.easysocial.networks.NodeAttribute;
import com.easysocial.networks.gephi.GephiNetwork;
import com.easysocial.networks.gephi.GephiVisualizer;

public class ImportTest2 {
	public static void main(String args[]) throws FileNotFoundException{
		FileInputStream in = new FileInputStream(new File("random.dat"));
		Scanner scan = new Scanner(in);
		
		int numOfFriends = Integer.parseInt(scan.nextLine());
		String myFriends [] = new String[numOfFriends];
		for(int i=0;i<myFriends.length;i++){
			myFriends[i] = scan.nextLine();
		}
		
		int numOfFriendships = Integer.parseInt(scan.nextLine());
		boolean friendshipMatrix[][] = new boolean[numOfFriends][numOfFriends];
		for(int i=0;i<numOfFriendships;i++){
			String [] friendship = scan.nextLine().split(",");
			int source = Integer.parseInt(friendship[0]);
			int target = Integer.parseInt(friendship[1]);
			friendshipMatrix[source][target]=true;
		}

		GephiNetwork network2 = GephiNetwork.newInstance(myFriends, friendshipMatrix);
		network2.analyze();
		
//		network2.partition();
//		network2.rank();
//		network2.layout();
//		network2.visualize();

/*		
		// builds a gephi network to take advantage of the gephi toolkit
		long before = Runtime.getRuntime().freeMemory()/(1024*1024);
		System.out.println("Before creating the gephi graph: "+before);
		GephiNetwork network = GephiNetwork.newInstance(myFriends, friendshipMatrix);
		long after = Runtime.getRuntime().freeMemory()/(1024*1024);
		System.out.println("After creating the gephi graph "+after);
		network.partition();
		network.rank();
		network.layout();
		after = Runtime.getRuntime().freeMemory()/(1024*1024);
		System.out.println("After processing the gephi graph "+after);

		network.visualize();
		after = Runtime.getRuntime().freeMemory()/(1024*1024);
		System.out.println("After visualizing the gephi graph "+after);
*/
		
		
		// sorts the network into communities where members within a community
		// are closer related than to members outside the community
		String [][] communities = network2.get(CommunityType.MODULARITY);
		
		// sorts the network members using their page rank scores
		// the first member in the array has the highest page rank score
		String pageRankList[] = network2.get(NodeAttribute.PAGERANK);
		
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
					network2.getLabel(highestId));
		}

	}
}
