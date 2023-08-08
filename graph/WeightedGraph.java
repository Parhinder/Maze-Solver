package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * <P>
 * This class represents a general "directed graph", which could be used for any
 * purpose. The graph is viewed as a collection of vertices, which are sometimes
 * connected by weighted, directed edges.
 * </P>
 * 
 * <P>
 * This graph will never store duplicate vertices.
 * </P>
 * 
 * <P>
 * The weights will always be non-negative integers.
 * </P>
 * 
 * <P>
 * The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.
 * </P>
 * 
 * <P>
 * The Weighted Graph will maintain a collection of "GraphAlgorithmObservers",
 * which will be notified during the performance of the graph algorithms to
 * update the observers on how the algorithms are progressing.
 * </P>
 */
public class WeightedGraph<V> {

	/*
	 * STUDENTS: You decide what data structure(s) to use to implement this class.
	 * 
	 * You may use any data structures you like, and any Java collections that we
	 * learned about this semester. Remember that you are implementing a weighted,
	 * directed graph.
	 */

	HashMap<V, HashMap<V, Integer>> vertices = new HashMap<V, HashMap<V, Integer>>();

	/*
	 * Collection of observers. Be sure to initialize this list in the constructor.
	 * The method "addObserver" will be called to populate this collection. Your
	 * graph algorithms (DFS, BFS, and Dijkstra) will notify these observers to let
	 * them know how the algorithms are progressing.
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	/**
	 * Initialize the data structures to "empty", including the collection of
	 * GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		vertices = new HashMap<V, HashMap<V, Integer>>();
		observerList = new HashSet<GraphAlgorithmObserver<V>>();
	}

	/**
	 * Add a GraphAlgorithmObserver to the collection maintained by this graph
	 * (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/**
	 * Add a vertex to the graph. If the vertex is already in the graph, throw an
	 * IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in the graph
	 */
	public void addVertex(V vertex) {
		// check if vertex already exists
		if (!(vertices.containsKey(vertex))) {
			vertices.put(vertex, new HashMap<V, Integer>());
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		// check if key exists
		return vertices.containsKey(vertex);
	}

	/**
	 * <P>
	 * Add an edge from one vertex of the graph to another, with the weight
	 * specified.
	 * </P>
	 * 
	 * <P>
	 * The two vertices must already be present in the graph.
	 * </P>
	 * 
	 * <P>
	 * This method throws an IllegalArgumentExeption in three cases:
	 * </P>
	 * <P>
	 * 1. The "from" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 2. The "to" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 3. The weight is less than 0.
	 * </P>
	 * 
	 * @param from   the vertex the edge leads from
	 * @param to     the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex is not in the graph, or
	 *                                  the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		// check params
		if (vertices.containsKey(from) && vertices.containsKey(to) && weight > 0) {
			// add edge
			vertices.get(from).put(to, weight);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * <P>
	 * Returns weight of the edge connecting one vertex to another. Returns null if
	 * the edge does not exist.
	 * </P>
	 * 
	 * <P>
	 * Throws an IllegalArgumentException if either of the vertices specified are
	 * not in the graph.
	 * </P>
	 * 
	 * @param from vertex where edge begins
	 * @param to   vertex where edge terminates
	 * @return weight of the edge, or null if there is no edge connecting these
	 *         vertices
	 * @throws IllegalArgumentException if either of the vertices specified are not
	 *                                  in the graph.
	 */
	public Integer getWeight(V from, V to) {
		// check if they contain keys
		if (vertices.containsKey(from) && vertices.containsKey(to)) {
			// check if valid
			if (!(vertices.get(from).containsKey(to))) {
				return null;
			}
			// return the vertex weight
			return vertices.get(from).get(to);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * <P>
	 * This method will perform a Breadth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyBFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without processing further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoBFS(V start, V end) {
		// notify collection of observers
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyBFSHasBegun();
		}
		// create queue for vertices that need to be visited and hashset for the ones
		// visited
		Queue<V> queue = new LinkedList<>();
		HashSet<V> visited = new HashSet<V>();
		// add to queue and mark vertex as visited
		queue.add(start);
		// check if empty
		while (!(queue.isEmpty())) {
			// get the element and remove it
			V currentVert = queue.poll();
			// check if visited set already contains the current vertex
			if (!(visited.contains(currentVert))) {
				// notify observers that we've just visited this vertex
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currentVert);
				}
				// add the vertex to visited
				visited.add(currentVert);

				// check if vertex is end vertex
				if (currentVert.equals(end)) {
					// call all observers notifying search is ended
					for (GraphAlgorithmObserver<V> observer : observerList) {
						observer.notifySearchIsOver();
					}
					// might cause problem not using break here
					return;
				}

				// get the adjacents of the vertex the loop through
				HashMap<V, Integer> adjacents = vertices.get(currentVert);
				for (V adjacent : adjacents.keySet()) {
					// check if adjacent is already in visited set otherwise add it
					if (!(visited.contains(adjacent))) {
						queue.add(adjacent);
					}
				}
			}
		}
	}

	/**
	 * <P>
	 * This method will perform a Depth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyDFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without visiting further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoDFS(V start, V end) {
		// notify collection of observers
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDFSHasBegun();
		}
		// create stack for the vertices and set for the visited ones
		Stack<V> stack = new Stack<>();
		HashSet<V> visited = new HashSet<V>();
		// add first vertex
		stack.push(start);
		// check if stack is empty
		while (!(stack.isEmpty())) {
			// get the top
			V currentVert = stack.pop();
			// check if it already contains
			if (!(visited.contains(currentVert))) {
				// notify observers current vertex being visited
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currentVert);
				}
				// add the vertex to visited
				visited.add(currentVert);

				// check if vertex is end vertex
				if (currentVert.equals(end)) {
					// call all observers notifying search is ended
					for (GraphAlgorithmObserver<V> observer : observerList) {
						observer.notifySearchIsOver();
					}
					// might cause problem not using break here
					return;
				}
				// if it is not the end add all adjacents of vertex
				HashMap<V, Integer> adjacents = vertices.get(currentVert);
				// loop through all adjacents
				for (V adjacent : adjacents.keySet()) {
					// if adjacent does not exist in set already then add to stack
					if (!(visited.contains(adjacent))) {
						stack.push(adjacent);
					}
				}

			}
		}

	}

	/**
	 * <P>
	 * Perform Dijkstra's algorithm, beginning at the "start" vertex.
	 * </P>
	 * 
	 * <P>
	 * The algorithm DOES NOT terminate when the "end" vertex is reached. It will
	 * continue until EVERY vertex in the graph has been added to the finished set.
	 * </P>
	 * 
	 * <P>
	 * Before the algorithm begins, this method goes through the collection of
	 * Observers, calling notifyDijkstraHasBegun on each Observer.
	 * </P>
	 * 
	 * <P>
	 * Each time a vertex is added to the "finished set", this method goes through
	 * the collection of Observers, calling notifyDijkstraVertexFinished on each one
	 * (passing the vertex that was just added to the finished set as the first
	 * argument, and the optimal "cost" of the path leading to that vertex as the
	 * second argument.)
	 * </P>
	 * 
	 * <P>
	 * After all of the vertices have been added to the finished set, the algorithm
	 * will calculate the "least cost" path of vertices leading from the starting
	 * vertex to the ending vertex. Next, it will go through the collection of
	 * observers, calling notifyDijkstraIsOver on each one, passing in as the
	 * argument the "lowest cost" sequence of vertices that leads from start to end
	 * (I.e. the first vertex in the list will be the "start" vertex, and the last
	 * vertex in the list will be the "end" vertex.)
	 * </P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end   special vertex used as the end of the path reported to observers
	 *              via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		// notify all of the observers the search begun
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun();
		}
		// create the costs of weights map and previous map for the vertices
		Map<V, V> previous = new HashMap<>();
		Map<V, Integer> cost = new HashMap<V, Integer>();
		// create visited set
		HashSet<V> visited = new HashSet<V>();

		// put first vertex into map with no cost
		cost.put(start, 0);

		// set all other vertexes to infinity or max value for their cost
		for (V vertex : vertices.keySet()) {
			// set all vertex cost to max value that is not the start
			if (!(vertex.equals(start))) {
				cost.put(vertex, Integer.MAX_VALUE);
			}
		}

		// set all the previous vertexes to null , there is no adjacents yet
		for (V vertex : vertices.keySet()) {
			previous.put(vertex, null);
		}

		// loop continues until all the vertices are in the visited set
		while (!(visited.containsAll(vertices.keySet()))) {
			// initialize the current lowest cost and vertex with smallest cost
			// also serve as trackers
			Integer currentLow = Integer.MAX_VALUE;
			V smallestCostVert = null;
			// loop through all the vertexes in the cost map
			for (V currentVert : cost.keySet()) {
				if (!(visited.contains(currentVert))) {
					// check if the cost is lower than the current lowest
					if (cost.get(currentVert) < currentLow) {
						// if true set the new lowest cost and vertex
						currentLow = cost.get(currentVert);
						smallestCostVert = currentVert;
					}
				}
			}
			//add vertex to visited now
			visited.add(smallestCostVert);
			//inform the observers the smallest vertex has been processed
			for(GraphAlgorithmObserver<V> observer: observerList){
				observer.notifyDijkstraVertexFinished(smallestCostVert, cost.get(smallestCostVert)); 
			}
			
			//traverse through the neighbors of the smallest vertex now
			for (V currentVert: vertices.get(smallestCostVert).keySet()) {
				if(!(visited.contains(currentVert))) {
					///check if the smallestCostVert weight is smaller than the current vertex weight
					if(cost.get(currentVert) > cost.get(smallestCostVert) + this.getWeight(smallestCostVert, currentVert)) {
						//replace the value
						cost.put(currentVert, cost.get(smallestCostVert) + this.getWeight(smallestCostVert, currentVert));
						//replace it with smaller vertex
						previous.put(currentVert, smallestCostVert);
					}
				}
			}
		}
		//use an array list and tracker to add the vertices in order that have the lowest path cost
		ArrayList<V> shortestPath = new ArrayList<V>();
		V currentNode = end;
		//add while it has no previous nodes
		do {
			//add the vertex
			shortestPath.add(0, currentNode);
			currentNode = previous.get(shortestPath.get(0));
		} while (currentNode != null);
		
		//notify all observers that the search is over
		for(GraphAlgorithmObserver<V> observer: observerList){ // Traversal over
			observer.notifyDijkstraIsOver(shortestPath);
		}
	}

}
