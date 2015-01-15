package com.easysocial.networks;

public interface Network {
	/**
	 * Adds a node to the network
	 * @param label
	 * @return the id of the new node
	 */
	String addNode(String label);
	
	/**
	 * Adds a directed edge in the network
	 * 
	 * @param source source node id
	 * @param target target node id
	 */
	void addEdge(String source, String target);
	
	/**
	 * Displays the network, works better if called after calling 
	 * {@link #layout()}, {@link #partition()} and {@link #rank()}
	 */
	void visualize();
	
	/**
	 * Assigns network nodes geometric positions so that the network looks reasonable.
	 * 
	 * For more, see <a href="http://en.wikipedia.org/wiki/Force-based_layout">here</a>
	 * 
	 * Note when new edges and/or nodes are added to a network, this method needs to be re-called.
	 *
	 */
	void layout();
	
	/**
	 * See {@link com.easysocial.networks.CommunityType}
	 * 
	 * Note when new edges and/or nodes are added to a network, this method needs to be re-called.
	 */
	void partition();
	
	/**
	 * Changes, in visualization, nodes' color and/or label size to 
	 * highlight more important nodes
	 */
	void rank();
	
	/**
	 * Calculates nodes' metrics in {@link com.easysocial.networks.NodeAttribute} and partitions
	 * the network using the methods in {@link com.easysocial.networks.CommunityType}.
	 * 
	 * Note when new edges and/or nodes are added to a network, this method needs to be re-called.
	 */
	void analyze();
}
