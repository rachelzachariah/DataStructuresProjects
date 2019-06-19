///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp.java
// File:             UndirectedGraph.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UndirectedGraph<V> implements GraphADT<V>{

	// Stores the vertices of this graph, and their adjacency lists.
	// It's protected rather than private so that subclasses can access it.
    protected HashMap<V, ArrayList<V>> hashmap;

    public UndirectedGraph() {
        this.hashmap = new HashMap<V, ArrayList<V>>();
    }

    public UndirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
        if (hashmap == null) throw new IllegalArgumentException();
        this.hashmap = hashmap;
    }

    /**
     * Adds vertex to the graphif not already present.
     * @param vertex to be added
     * @return true if and only if vertex was successfully added.
     */
    @Override
    public boolean addVertex(V vertex) {
    	if (vertex == null) throw new IllegalArgumentException();
    	if (hashmap.containsKey(vertex))
    		return false;
    	else{
    		hashmap.put(vertex, new ArrayList<V>());
    		return true;
    	}
    }

    /**
     * Creates a new edge between vertices, unless vertices are equal or an edge 
     * already exists between them.
     * @param vertices between which edges should be added
     * @return true if and only if the edge was successfully added.
     */
    @Override
    public boolean addEdge(V v1, V v2) {
    	if (v1==null||v2==null|| hashmap.get(v1)==null || hashmap.get(v2)==null)
    		throw new IllegalArgumentException();
    	if (!v1.equals(v2) && !hashmap.get(v1).contains(v2)){
    		hashmap.get(v1).add(v2);
    		hashmap.get(v2).add(v1);
    		return true;
    	}
        return false;
    }

    /**
     * Returns the immediate neighbors of a vertex
     * @param vertex whose neighbors are to be returned
     * @return Set of neighbor vetrices
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
    	if (vertex==null || hashmap.get(vertex)==null)
    		throw new IllegalArgumentException();
    	else{
    		HashSet<V> set = new HashSet<V>();
    		ArrayList<V> vertices = hashmap.get(vertex);
    		for (int i=0; i<vertices.size(); i++)
    			set.add(vertices.get(i));
    		return set;
    	}
    }
    
    /**
     * Removes edge between two vertices.
     * @param vertices between which the edge is to be removed.
     */
    @Override
    public void removeEdge(V v1, V v2) {
        if (v1==null || v2==null || hashmap.get(v1)==null || hashmap.get(v2)==null)
    		return;
        hashmap.get(v1).remove(v2);
        hashmap.get(v2).remove(v1);
        return;
    }
    
    /**
     * Returns all the vertices in the graph.
     * @return Set of vertices.
     */
    @Override
    public Set<V> getAllVertices() {
        return hashmap.keySet();
    }

    /* (non-Javadoc)
     * Returns a print of this graph in adjacency lists form.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        for (V vertex: this.hashmap.keySet()) {
            writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
        }
        return writer.toString();
    }

}
