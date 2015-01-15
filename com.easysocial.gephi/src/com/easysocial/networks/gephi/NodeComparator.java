package com.easysocial.networks.gephi;

import java.util.Comparator;

import org.gephi.graph.api.Node;

class NodeComparator implements Comparator<Node> {
	private String heading;

	public NodeComparator(String heading){
		this.heading = heading;
	}
	
	@Override
	public int compare(Node n1, Node n2) {
		float val1 = Float.parseFloat(n1.getAttributes().getValue(heading).toString());
		float val2 = Float.parseFloat(n2.getAttributes().getValue(heading).toString());

		if(val1>val2){
			return -1;
		}else if(val1<val2){
			return 1;
		}else{
			return 0;
		}
	}

}
