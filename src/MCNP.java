import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import jdd.graph.Edge;
import jdd.graph.Graph;
import jdd.graph.Node;
import jdd.graph.Partition;
import jdd.graph.StronglyConnectedComponent;

public class MCNP {

	private Graph g;
	private Map<String,Set<String>> map;
	private int partitionId;
	private List<String> graphRoots;

	public MCNP(Graph g) {

		this.g = g;
		this.map = null;
		this.partitionId = 0;
		//		this.graphRoots = getGraphRoots();
		Partition p = StronglyConnectedComponent.tarjan(g, (Node)g.getNodes().get(0));
		checkPartition(p);
	}

	public MCNP() {
		this.g = new Graph(true);
		this.map = null;
		this.partitionId = 0;

	}

	private void findPartitions() {
		Partition p = StronglyConnectedComponent.tarjan(g, (Node)g.getNodes().get(0));
		checkPartition(p);
	}

	private void addClass(String c) {
		g.addNode(new Node(0, c));
	}

	public void addClasses(List<String> classes) {

		for (int i = 0; i < classes.size(); i++) {
			addClass(classes.get(i));
		}
	}

	public void addRelationship11(String class1, String class2) {

		Node n1 = g.findNode(class1);
		Node n2 = g.findNode(class2);

		g.addEdge(n1, n2);
		g.addEdge(n2, n1);
	}

	public void addRelationship1N(String class1, String classN) {

		Node n1 = g.findNode(class1);
		Node n2 = g.findNode(classN);

		g.addEdge(n1, n2);
	}

	public void addRelationshipN1(String classN, String class1) {

		Node n1 = g.findNode(classN);
		Node n2 = g.findNode(class1);

		g.addEdge(n1, n2);
	}

	private void checkPartition(Partition p) {

		Vector<Node> nodes = g.getNodes();

		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {

				if (p.inSamePartition(nodes.get(i), nodes.get(j))) {
					checkNodes(nodes.get(i).label, nodes.get(j).label);
				}
			}
		}
	}

	private void checkNodes(String n1, String n2) {

		if (map == null) {
			map = new HashMap<String, Set<String>>();
			Set<String> set = new HashSet<String>();
			set.add(n1);
			set.add(n2);
			map.put(Integer.toString(partitionId++), set);
		}
		else {

			List<String> keys = new ArrayList<String>(map.keySet());

			int i = 0;
			String key;
			Set<String> values = null;

			boolean partitionContainsNodes = false;
			while (!partitionContainsNodes && i < keys.size()) {

				key = keys.get(i);
				values = map.get(key);
				partitionContainsNodes = values.contains(n1) || values.contains(n2);
				i++;
			}

			if (partitionContainsNodes) {
				values.add(n1);
				values.add(n2);
			}
			else {
				Set<String> set = new HashSet<String>();
				set.add(n1);
				set.add(n2);
				map.put(Integer.toString(partitionId++), set);
			}
		}
	}

	private boolean isPartitionRoot(Set<String> partition) {

		for (String root : this.graphRoots) {
			if (partition.contains(root)) {
				return true;
			}
		}
		return false;
	}

	private List<String> getGraphRoots() {

		List<String> roots = new ArrayList<String>();
		Vector<Node> nodes = g.getNodes();
		for (Node node : nodes) {

			if (isRoot(node)) {
				roots.add(node.label);
			}
		}
		return roots;
	}

	private boolean isRoot(Node node) {
		return node.firstIn == null;
	}

	public void printPartition() {

		Set<String> keys = map.keySet();
		for (String key : keys) {
			System.out.println(map.get(key));
		}
	}

	private int getNodeDegree(String nodeName) {

		return getNodeDegree(g.findNode(nodeName));
	}

	private int getNodeDegree(Node node) {

		int cont = 0;
		Vector<Edge> edges = g.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);

			Node nodeIn = edge.n1;
			Node nodeOut = edge.n2;
			if (node.getLabel().equalsIgnoreCase(nodeIn.getLabel())) {
				cont++;
			}
			if (node.getLabel().equalsIgnoreCase(nodeOut.getLabel())) {
				cont++;
			}
		}
		return cont;
	}

	// In-Nodes of a "node" without consider its "partition" nodes
	private List<String> getNodeIn(Node node, Set<String> partition) {
		List<String> nodeIns = new ArrayList<String>();

		Vector<Edge> edges = g.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);

			Node nodeIn = edge.n1;
			Node nodeOut = edge.n2;
			if (node.getLabel().equalsIgnoreCase(nodeOut.getLabel()) && 
					!partition.contains(nodeIn.getLabel())) {
				nodeIns.add(nodeIn.getLabel());
			}
		}
		return nodeIns;
	}

	private List<String> getPartitionIns(Set<String> partition) {
		List<String> ins = new ArrayList<String>();

		for (String n  : partition) {

			Node node = g.findNode(n);
			ins.addAll(getNodeIn(node, partition));

		}
		//		System.out.println(ins);
		return ins;
	}

	public Map<String, Set<String>> getSCCRoots() {
		Map<String, Set<String>> partitionsIn = new HashMap<String, Set<String>>();

		Set<String> keys = map.keySet();
		for (String key : keys) {

			Set<String> value = map.get(key);
			List<String> partitionIns = getPartitionIns(value);
			if (partitionIns.isEmpty()) {
				partitionsIn.put(key, value);
			}
		}
		return partitionsIn;
	}

	public List<String> getSCCMainRoots() {
		
		findPartitions();

		List<String> mainRoots = new ArrayList<String>();

		Map<String, Set<String>> partitions = getSCCRoots();
		for (Set<String> partition : partitions.values()) {
			
			System.out.println("partition " + partition);
			
			int maxDegree = 0;
			String nodeNameWithMaxDegree = "";
			for (String nodeName : partition) {
				int degree = getNodeDegree(nodeName);
				if (degree > maxDegree) {
					maxDegree = degree;
					nodeNameWithMaxDegree = nodeName;
				}
			}
			mainRoots.add(nodeNameWithMaxDegree);
		}
		return mainRoots;
	}


	public static void main(String[] args) {


		//		Graph g2 = new Graph(true);
		//		Node n4 = new Node(0, "n4");
		//		Node n5 = new Node(0, "n5");
		//		Node n6 = new Node(0, "n6");
		//		Node n2 = new Node(0, "n2");
		//		Node n3 = new Node(0, "n3");
		//		Node n7 = new Node(0, "n7");
		//		Node n1 = new Node(0, "n1");
		//		Node n0 = new Node(0, "n0");
		//		Node n8 = new Node(0, "n8");
		//
		//		g2.addNode(n2);
		//		g2.addNode(n4);
		//		g2.addNode(n5);
		//		g2.addNode(n6);
		//		g2.addNode(n0);
		//		g2.addNode(n7);
		//		g2.addNode(n1);
		//		g2.addNode(n3);
		////		g2.addNode(n8);
		//
		//		g2.addEdge(n3, n2);
		//		g2.addEdge(n2, n3);
		//		g2.addEdge(n5, n6);
		//		g2.addEdge(n6, n7);
		//		g2.addEdge(n4, n3);
		//		g2.addEdge(n3, n4);
		//		g2.addEdge(n0, n4);
		//		g2.addEdge(n2, n1);
		//		g2.addEdge(n1, n2);
		//		g2.addEdge(n4, n5);
		//		g2.addEdge(n7, n1);
		////		g2.addEdge(n8, n0);



		Graph g = new Graph(true);
		Node n1 = new Node(0, "n1");
		Node n2 = new Node(0, "n2");
		Node n3 = new Node(0, "n3");
		Node n4 = new Node(0, "n4");
		Node n5 = new Node(0, "n5");
		Node n6 = new Node(0, "n6");
		Node n7 = new Node(0, "n7");
		Node n8 = new Node(0, "n8");
		Node n9 = new Node(0, "n9");
		Node n10 = new Node(0, "n10");

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		g.addNode(n7);
		g.addNode(n8);
		g.addNode(n9);
		g.addNode(n10);

		g.addEdge(n1, n2);
		g.addEdge(n2, n1);
		g.addEdge(n2, n3);
		g.addEdge(n3, n2);
		g.addEdge(n1, n4);
		g.addEdge(n4, n5);
		g.addEdge(n5, n6);
		g.addEdge(n6, n4);
		g.addEdge(n3, n7);
		g.addEdge(n7, n8);
		g.addEdge(n8, n7);
		g.addEdge(n9, n8);
		g.addEdge(n9, n10);
		g.addEdge(n10, n10);
		//
		//		Partition p = StronglyConnectedComponent.tarjan(g, (Node)g.getNodes().get(0));
		//		p.show();

		//		Graph g = new Graph(true);
		//		Node a1 = new Node(0, "a1");
		//		Node a2 = new Node(0, "a2");
		//		Node a3 = new Node(0, "a3");
		//		Node a4 = new Node(0, "a4");
		//		Node a5 = new Node(0, "a5");
		//		Node a6 = new Node(0, "a6");
		//		Node a7 = new Node(0, "a7");
		//		
		//		g.addNode(a4);
		//		g.addNode(a5);
		//		g.addNode(a6);
		//		g.addNode(a7);
		//		g.addNode(a1);
		//		g.addNode(a2);
		//		g.addNode(a3);
		//
		//		
		//		g.addEdge(a3, a2);
		//		g.addEdge(a3, a4);
		//		g.addEdge(a4, a3);
		//		g.addEdge(a6, a7);
		//		g.addEdge(a1, a2);
		//		g.addEdge(a2, a1);
		//		g.addEdge(a2, a3);
		//		g.addEdge(a4, a5);
		//		g.addEdge(a5, a6);


		MCNP scc = new MCNP(g);
		scc.printPartition();
		System.out.println("----");
		System.out.println(scc.getSCCRoots());
		//		System.out.println(scc.getNodeDegree(a1));
		System.out.println(scc.getSCCMainRoots());
	}

}
