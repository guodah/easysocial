package com.easysocial.networks.gephi;

import java.util.ArrayList;
import java.util.HashMap;

public class GephiNetworkBuilder {
	private GephiNetwork network;
	private HashMap<String, String> nodes;
	private HashMap<String, ArrayList<String>> edges;
	
	public GephiNetworkBuilder(){
		nodes = new HashMap<String, String>();
		edges = new HashMap<String, ArrayList<String>>();
	}
	
	public void addNode(String id, String label){
		nodes.put(id,  label);		
	}
	
	public void addEdge(String id1, String id2){
		if(!nodes.containsKey(id1) || !nodes.containsKey(id2)){
			return;
		}
		
		if(!edges.containsKey(id1)){
			edges.put(id1, new ArrayList<String>());
		}
		edges.get(id1).add(id2);
	}
	
	public GephiNetwork build(){
		String labels[] = new String[nodes.size()];
		boolean edges[][] = new boolean[nodes.size()][nodes.size()];
		HashMap<String, Integer> indices = new HashMap<String, Integer>();
		int count = 0;
		for(String id:nodes.keySet()){
			indices.put(id,  count);
			labels[count++] = nodes.get(id);
		}

		for(String id1:this.edges.keySet()){
			for(String id2:this.edges.get(id1)){
				edges[indices.get(id1)][indices.get(id2)] = true;
			}
		}
		
		return GephiNetwork.newInstance(labels, edges);
	}
}
