package com.easysocial.networks;

/**
 * This enumeration summarizes the ways of partitioning a network:
 * <ol>
 * <li> Modularity: See <a href="http://en.wikipedia.org/wiki/Modularity_(networks)">here for more</a>
 * <li> Strongly Connected Components: See <a href="http://en.wikipedia.org/wiki/Strongly_connected_component">here for more</a>
 * <li> Weakly Connected Components: See <a href="http://en.wikipedia.org/wiki/Connected_component_(graph_theory)">here for more</a>
 * </ol> 
 * 
 * @author Dahai Guo
 *
 */
public enum CommunityType {
	MODULARITY,
	STRONG_CONNECTED_COMPONENTS,
	WEAK_CONNECTED_COMPONENTS
}
