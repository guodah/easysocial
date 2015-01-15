package com.easysocial.networks.test;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//import com.easysocial.apis.EasySocial;
import com.easysocial.networks.gephi.GephiNetwork;

class WholeNetwork {
	/*
	public static void randomizeGML(boolean [][] friendships, OutputStream out){
		String firstNames[] = {"Alan", "Aaron", "Aden", "Arden", "Abbe",
				"Bobby", "Bill", "Baron", "Bale", "Ben",
				"Carl", "Cacey", "Cain", "Camira", "Calin",
				"Dafni", "Debra", "Dell", "Darko", "Dorsey"
		};
		String lastNames[] = {"Abbott", "Abina", "Abston", "Achoe", "Acker",
				"Babin", "Baca", "Bader", "Badley", "Bahl",
				"Cabot", "Caby", "Caccia", "Caffey", "Camby",
				"Dakon", "Dailey", "Dale", "Dalton", "Dando" 
		};
		
		String names[] = new String[friendships.length];
		for(int i=0;i<names.length;i++){
			names[i] = firstNames[i/20]+" "+lastNames[i%20];
		}
		
		Formatter format = new Formatter(out);
		format.format("graph\n[\ndirected 1\n");
		for(int i=0;i<names.length;i++){
			format.format("node\n[\n id %d\n label \"%s\"\n]\n", i,names[i]);
		}
		
		for(int i=0;i<friendships.length;i++){
			for(int j=0;j<friendships[i].length;j++){
				if(friendships[i][j])
					format.format("edge\n[\n source %d\n target %d\n]\n", i,j);
			}
		}
		format.format("]");
		format.close();
	}
	public static void main(String args[]) throws FileNotFoundException{
		
		String [] friends = EasySocial.facebook.getMyFriends();

		// allPeople stores each friend's position in array friends
		Map<String, Integer> allPeople = new HashMap<String, Integer>();
		for(int i=0;i<friends.length; 
				allPeople.put(friends[i], i),i++);
		
		// this boolean array will record friendship relationship
		// if friends[i] and friends[j] are also friends, friendships[i][j] is true
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
		
		randomizeGML(friendships, new FileOutputStream(new File("random.gml")));
		randomizeTxt(friendships, new FileOutputStream(new File("random.dat")));
		
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
		
		// builds a gephi network to take advantage of the gephi toolkit
//		GephiNetwork network = GephiNetwork.newInstance(names, friendships);
		GephiNetwork network = GephiNetwork.newInstance(myFriends, friendshipMatrix);
		network.partition();
		network.rank();
		network.layout();
		network.visualize();
				
	}
	private static void randomizeTxt(boolean[][] friendships,
			FileOutputStream out) {
		String firstNames[] = {"Alan", "Aaron", "Aden", "Arden", "Abbe",
				"Bobby", "Bill", "Baron", "Bale", "Ben",
				"Carl", "Cacey", "Cain", "Camira", "Calin",
				"Dafni", "Debra", "Dell", "Darko", "Dorsey"
		};
		String lastNames[] = {"Abbott", "Abina", "Abston", "Achoe", "Acker",
				"Babin", "Baca", "Bader", "Badley", "Bahl",
				"Cabot", "Caby", "Caccia", "Caffey", "Camby",
				"Dakon", "Dailey", "Dale", "Dalton", "Dando" 
		};
		
		String names[] = new String[friendships.length];
		for(int i=0;i<names.length;i++){
			names[i] = firstNames[i/20]+" "+lastNames[i%20];
		}
		
		Formatter format = new Formatter(out);
		format.format("%d\n", names.length);
		for(int i=0;i<names.length;i++){
			format.format("%s\n", names[i]);
		}
		
		int numOfFriendships=0;
		for(int i=0;i<friendships.length;i++){
			for(int j=0;j<friendships[i].length;j++){
				if(friendships[i][j])
					numOfFriendships++;
			}
		}
		format.format("%d\n", numOfFriendships);
		for(int i=0;i<friendships.length;i++){
			for(int j=0;j<friendships[i].length;j++){
				if(friendships[i][j])
					format.format("%d,%d\n",i,j);
			}
		}
				
		format.close();
	}
	*/
}
