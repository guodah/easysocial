package com.easysocial.networks;

/**
 * This enumeration summarizes several metrics for comparing nodes in a network:
 * <ol>
 * <li> Eccentricity: See <a href="http://mathworld.wolfram.com/GraphEccentricity.html">here for more</a>
 * <li> Closeness: See <a href="http://en.wikipedia.org/wiki/Centrality#Closeness_centrality">here for more</a>
 * <li> Betweenness: See <a href="http://en.wikipedia.org/wiki/Centrality#Betweenness_centrality">here fore more</a>
 * <li> Pagerank: See <a href="http://en.wikipedia.org/wiki/PageRank">here for more</a>
 * <li> Degree: See <a href="http://en.wikipedia.org/wiki/Degree_(graph_theory)">here for more</a>
 * <li> In Degree: See <a href="http://en.wikipedia.org/wiki/Indegree#Indegree_and_outdegree">here for more</a>
 * <li> Out Degree: See <a href="http://en.wikipedia.org/wiki/Indegree#Indegree_and_outdegree">here for more</a>
 * <li> Weighted In Degree : See <a href="http://en.wikipedia.org/wiki/Indegree#Indegree_and_outdegree">here for more</a>
 * <li> Weighted Out Degree : See <a href="http://en.wikipedia.org/wiki/Indegree#Indegree_and_outdegree">here for more</a>
 * </ol>
 * @author Dahai Guo
 *
 */
public enum NodeAttribute {
	ECCENTRICITY,
	CLOSENESS,
	BETWEENNESS,
	PAGERANK,
	DEGREE,
	IN_DEGREE,
	OUT_DEGREE,
	WEIGHTED_DEGREE,
	WEIGHTED_IN_DEGREE,
	WEIGHTED_OUT_DEGREE
}
