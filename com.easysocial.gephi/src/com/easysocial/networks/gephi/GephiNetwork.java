package com.easysocial.networks.gephi;

import java.awt.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.graph.api.Attributes;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.ConnectedComponents;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.gephi.statistics.plugin.PageRank;
import org.gephi.statistics.plugin.WeightedDegree;
import org.openide.util.Lookup;

import com.easysocial.networks.CommunityType;
import com.easysocial.networks.Network;
import com.easysocial.networks.NodeAttribute;

/**
 * This implementation of {@link com.easysocial.networks.Network} is based on
 * the <a href="https://gephi.org/toolkit/"> Gephi</a> toolkit.
 * 
 * @author Dahai Guo
 *
 */
public class GephiNetwork implements Network{
	
	private GraphModel graph;
	private Map<String, Node> nodes;	
	private Map<NodeAttribute, String[]> nodesView;
	private Map<CommunityType, String[][]> communityView;
	private String name;
	private GephiVisualizer visualizer;
	
	/**
	 * Creates an empty graph
	 */
	private GephiNetwork(String name){
		this.name = name;
		graph = GephiContext.getGraph();
		nodes = new HashMap<String, Node>();
		visualizer = new GephiVisualizer();
	}
	
	/**
	 * Imports a graph from a Gephi recognized format.
	 * 
	 * @param path
	 * @return an object of GephiNetwork or null if the path is wrong or
	 * the file is invalid
	 */
	public static GephiNetwork newInstance(String path){
		
		ImportController importController = null;
		Container container;
		GephiNetwork network;
        try {
            File file = new File(path);
            network = new GephiNetwork(file.getName());
    		importController = Lookup.getDefault().lookup(ImportController.class);
            
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force DIRECTED
            container.setAllowAutoNode(false);  //Don't create missing nodes
        } catch (Exception ex) {
            return null;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), network.getGephiWorkspace());
        
        Iterator<Node> nodes = network.graph.getDirectedGraph().getNodes().iterator();
        while(nodes.hasNext()){
        	Node node = nodes.next();
        	network.nodes.put(String.valueOf(node.getId()), node);
        }
        return network;
	}
	
	public Workspace getGephiWorkspace(){
		return graph.getWorkspace();
	}
	
	/**
	 * Builds a network with node labels and their adjacency matrix.
	 * 
	 * It uses the first label to name the graph. 
	 * 
	 * @param labels node labels
	 * @param edges adjacency matrix
	 * @return
	 */
	public static GephiNetwork newInstance(String [] labels, boolean [][] edges){
		if(!check(labels, edges)){
			return null;
		}
		return newInstance(labels[0]+"...", labels, edges);
	}
	/**
	 * Creates a graph and populates it with nodes and edges.
	 * 
	 * @param labels a list of node labels
	 * @param edges a boolean matrix. An edge that connects node i and node j is 
	 * 			represented by edges[i][j] = true;
	 * @return an object of GephiNetwork or null if the arguments are incorrect.
	 */
	public static GephiNetwork newInstance(String name, String [] labels, boolean [][] edges){
		
		if(!check(labels, edges)){
			return null;
		}
		
		GephiNetwork network = new GephiNetwork(name);
		String ids[] = new String[labels.length];
		for(int i=0;i<ids.length;i++){
			String label = labels[i];
			ids[i] = network.addNode(label);
		}
		
		for(int i=0;i<edges.length;i++){
			for(int j=0;j<edges.length;j++){
				if(edges[i][j]){
					network.addEdge(ids[i],ids[j]);
				}
			}
		}
		return network;
	}
	
	private static boolean check(String[] labels, boolean[][] edges) {
		if(labels==null || labels.length==0 ||
				edges==null || edges.length==0 || labels.length!=edges.length){
			return false;
		}
		
		int size = labels.length;
		for(int i=0;i<edges.length;i++){
			if(edges[i].length!=size){
				return false;
			}
		}
		return true;
	}

	@Override
	public String addNode(String label) {
		Node node = graph.factory().newNode(label);
		node.getNodeData().setLabel(label);
		
		String id = new Integer(node.getId()).toString(); 
		nodes.put(id, node);
		graph.getDirectedGraph().addNode(node);
		return id;
	}

	@Override
	public void addEdge(String source, String target) {
		Node n1 = nodes.get(source);
		Node n2 = nodes.get(target);
		Edge e = graph.factory().newEdge(n1, n2,1f,true);
		graph.getDirectedGraph().addEdge(e);
	}

	public String getName(){
		return name;
	}
	
	@Override
	public void visualize() {
		visualizer.visualize(this);
	}

	@Override
	public void layout() {
		GephiContext.setWorkspace(getGephiWorkspace());
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
        layout.setGraphModel(graph);
        layout.resetPropertiesValues();
        layout.setOptimalDistance(300f);

        layout.initAlgo();
        for (int i = 0; i < 300 && layout.canAlgo(); i++) {
            layout.goAlgo();
        }
        layout.endAlgo();

	}

	/**
	 * Partitions the nodes using {@link com.easysocial.networks.CommunityType#MODULARITY} and
	 * colors the nodes differently according to their partitions.
	 * 
	 */
	@Override
	public void partition() {
//		System.out.println("Partition");
		
		GephiContext.setWorkspace(getGephiWorkspace());
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		Modularity modularity = new Modularity();
		modularity.execute(graph, attributeModel);

		//Partition with 'modularity_class', just created by Modularity algorithm
		AttributeColumn modColumn = attributeModel.getNodeTable().getColumn(Modularity.MODULARITY_CLASS);
		Partition p2 = partitionController.buildPartition(modColumn, graph.getDirectedGraph());
//		System.out.println(p2.getPartsCount() + " partitions found");
		NodeColorTransformer nodeColorTransformer2 = new NodeColorTransformer();
		nodeColorTransformer2.randomizeColors(p2);
		partitionController.transform(p2, nodeColorTransformer2);
	}

	/**
	 * Ranks the node label according to their {@link com.easysocial.networks.NodeAttribute#BETWEENNESS}.
	 */
	@Override
	public void rank() {
		GephiContext.setWorkspace(getGephiWorkspace());		
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
     
        RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);

        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graph, attributeModel);

        //Rank size by centrality
        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(
        		Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
        sizeTransformer.setMinSize(3);
        sizeTransformer.setMaxSize(20);
        rankingController.transform(centralityRanking,sizeTransformer);		
	}

	@Override
	public void analyze() {
//		System.out.println("Analyze");

		GephiContext.setWorkspace(getGephiWorkspace());		
		// eccentricity, closeness, betweeness
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graph, attributeModel);

        // modularity class
		Modularity modularity = new Modularity();
		modularity.execute(graph, attributeModel);

		// pagerank
		PageRank pagerank = new PageRank();
		pagerank.execute(graph, attributeModel);
		
		// degrees
		Degree degree = new Degree();
		degree.execute(graph, attributeModel);
		
		// weighted degrees
		WeightedDegree weightedDegree = new WeightedDegree();
		weightedDegree.execute(graph, attributeModel);

		// component id
        ConnectedComponents connectedComponents = new ConnectedComponents();
        connectedComponents.execute(graph, attributeModel);
		
        packNodesView();
        packCommunityView();
	}

	private void packCommunityView() {
		communityView = new HashMap<CommunityType, String[][]>();
		Node [] nodeArray = new Node[nodes.size()];
		int count=0;
		for(String key:nodes.keySet()){
			nodeArray[count++] = nodes.get(key);
		}
		
		for(CommunityType type: CommunityType.values()){
			String heading = map(type);
			Map<String, ArrayList<String>> community = new HashMap<String, ArrayList<String>>();

			for(int i=0;i<nodeArray.length;i++){
				String num = nodeArray[i].getAttributes().getValue(heading).toString();
				if(!community.containsKey(num)){
					community.put(num, new ArrayList<String>());
				}
				community.get(num).add(new Integer(nodeArray[i].getId()).toString());
			}

			String[][] communityArray = new String[community.size()][];
			count=0;
			for(String key:community.keySet()){
				ArrayList<String> part = community.get(key);
				communityArray[count] = new String[part.size()];
				part.toArray(communityArray[count]);
				count++;
			}
			Arrays.sort(communityArray, new Comparator<String[]>(){
				@Override
				public int compare(String[] arg0, String[] arg1) {
					return arg1.length-arg0.length;
				}
				
			});
			communityView.put(type, communityArray);
		}
		
	}

	private void packNodesView() {
		nodesView = new HashMap<NodeAttribute, String[]>();
		Node [] nodeArray = new Node[nodes.size()];
		int count=0;
		for(String key:nodes.keySet()){
			nodeArray[count++] = nodes.get(key);
		}
	
		for(NodeAttribute attribute: NodeAttribute.values()){
			String heading = map(attribute);
			
			Arrays.sort(nodeArray, new NodeComparator(heading));
			String [] labels = new String[nodeArray.length];
			for(int i=0;i<nodeArray.length;i++){
			//	labels[i] = nodeArray[i].getNodeData().getLabel();
				labels[i] = new Integer(nodeArray[i].getId()).toString();
			}
			nodesView.put(attribute, labels);
		}
		
	}

	private String map(CommunityType type){
		if(type==CommunityType.STRONG_CONNECTED_COMPONENTS){
			return ConnectedComponents.STRONG;
		}else if(type==CommunityType.WEAK_CONNECTED_COMPONENTS){
			return ConnectedComponents.WEAKLY;
		}else {//if(type==CommunityType.MODULARITY){
			return Modularity.MODULARITY_CLASS;
		}
	}
	
	private String map(NodeAttribute attribute) {
		if(attribute==NodeAttribute.BETWEENNESS){
			return GraphDistance.BETWEENNESS;
		}else if(attribute==NodeAttribute.ECCENTRICITY){
			return GraphDistance.ECCENTRICITY;
		}else if(attribute==NodeAttribute.CLOSENESS){
			return GraphDistance.CLOSENESS;
		}else if(attribute==NodeAttribute.DEGREE){
			return Degree.DEGREE;
		}else if(attribute==NodeAttribute.IN_DEGREE){
			return Degree.INDEGREE;
		}else if(attribute==NodeAttribute.OUT_DEGREE){
			return Degree.OUTDEGREE;
		}else if(attribute==NodeAttribute.WEIGHTED_IN_DEGREE){
			return WeightedDegree.WINDEGREE;
		}else if(attribute==NodeAttribute.WEIGHTED_OUT_DEGREE){
			return WeightedDegree.WOUTDEGREE;
		}else if(attribute==NodeAttribute.WEIGHTED_DEGREE){
			return WeightedDegree.WDEGREE;
		}else{
			return PageRank.PAGERANK;
		}
	}

	/**
	 * Given an attribute in {@link com.easysocial.networks.NodeAttribute},
	 * returns the list of node ids in the descending order of that attribute.
	 * @param attribute
	 * @return
	 */
	public String [] get(NodeAttribute attribute){
		if(nodesView==null){
			this.analyze();
		}
		return nodesView.get(attribute);
	}

	/**
	 * Given a community type in {@link com.easysocial.networks.CommunityType},
	 * returns the list of partitions in form of a two dimensional array of node
	 * ids.
	 * 
	 * The communities are sorted in the descending order of the size.
	 *  
	 * @param type
	 * @return
	 */
	public String[][] get(CommunityType type) {
		if(communityView==null){
			this.analyze();
		}
		return communityView.get(type);
	}
	
	/**
	 * Finds the label given a node id.
	 * @param id
	 * @return
	 */
	public String getLabel(String id){
		return nodes.get(id).getNodeData().getLabel();
	}
	
	public void close(){
		GephiContext.closeWorkSpace(graph.getWorkspace());
	}
}
