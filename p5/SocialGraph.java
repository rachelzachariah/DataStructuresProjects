///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp.java
// File:             SocialGraph.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class SocialGraph extends UndirectedGraph<String> {

    /**
     * Creates an empty social graph.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     */
    public SocialGraph() {
        super();
    }

    /**
     * Creates a graph from a preconstructed hashmap.
     * This method will be used to test your submissions. You will not find this
     * in a regular ADT.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     * DO NOT CALL THIS CONSTRUCTOR ANYWHERE IN YOUR SUBMISSION.
     * 
     * @param hashmap adjacency lists representation of a graph that has no
     * loops and is not a multigraph.
     */
    public SocialGraph(HashMap<String, ArrayList<String>> hashmap) {
        super(hashmap);
    }
    
    /**
     * Returns a user's friends of friends.
     * @param person user whose friends of friends are to be found.
     * @return Set consisting if the user's friends of friends.
     */
    public Set<String> friendsOfFriends(String person) {
    	if (person==null||!this.getAllVertices().contains(person))
    		throw new IllegalArgumentException();
        Queue<String> frontier = new ArrayDeque<String>(); 
        Set<String> explored = new HashSet<String>(); // keeps track of visited vertices.
        Set<String> fof = new HashSet<String>();
        Set<String> neighbors1 =  this.getNeighbors(person);
        Set<String> neighbors2;
        
        explored.add(person); 
        
        for (String i:neighbors1){ //breadth first search of depth 1
        	frontier.add(i);
        	explored.add(i);  
        }
        for (@SuppressWarnings("unused") String i:neighbors1){ 
        	//dequeues everything at depth from frontier, so frontier holds the req.info
        	neighbors2 = this.getNeighbors(frontier.remove());
        	for (String j:neighbors2){ //breadth first search of depth 2
            	if (!explored.contains(j))
            		frontier.add(j);
            		explored.add(j);
            }
        }
        while(!frontier.isEmpty()){
        	fof.add(frontier.remove());
        }
        return fof;
    }

    /**
     * Returns the shortest path between two people or null if there is no path 
     * between them, if both people exist in this graph and they are not the 
     * same person.
     * @param pFrom User from where the path begins.
     * @param pTo User at which the path ends.
     * @return List for the path beginning with the personFrom and ending with personTo.
     */
    public List<String> getPathBetween(String pFrom, String pTo) {
    	if (pTo==null || pFrom==null || pTo.equals(pFrom))
    		throw new IllegalArgumentException();
    	try{ 
    		this.getNeighbors(pFrom);
    		this.getNeighbors(pTo);
    	}catch(IllegalArgumentException e){
    		throw new IllegalArgumentException();
    	}
    	
    	Queue<String> frontier = new ArrayDeque<String>(); 
    	Set<String> explored = new HashSet<String>(); //maintains record of visited vertices
    	HashMap<String,String> preds = new HashMap<String,String>(); 
    	//keeps track of predecessors so  a path can be retraced.
    	
    	boolean found = false; //target not found yet
    	
    	frontier.add(pFrom);
    	explored.add(pFrom);
    	preds.put(pFrom, null); //pFrom has no preds
  
    	while(!found && !frontier.isEmpty()){ 
    		//breadth first search, stop when target found or nowhere else to go
    		String curr = frontier.remove();
    		Set<String> neighbors =  this.getNeighbors(curr);
    		for (String i:neighbors){
    			if (!explored.contains(i)){
    				preds.put(i, curr);
    				frontier.add(i);
    				explored.add(i);
    				if (i.equals(pTo))
    					found = true; //target found
    			}
    		}
    	}
    	
    	Stack<String> tmp = new Stack<String>(); //to retrace path back to pFrom
    	List<String> path = new ArrayList<String>();
    	String u = pTo;
    	
    	while(u!=null){
    		tmp.push(u);
    		u = preds.get(u); //trace back
    	}
    	while (!tmp.isEmpty()){
    		path.add(tmp.pop());
    	} 
    	
    	if (path.size()>=2) return path;
    	else return null;
    }

    /**
     * Returns a pretty-print of this graph in adjacency matrix form.
     * People are sorted in alphabetical order, "X" denotes friendship.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     *
     * NOTE: this method assumes that the internal hashmap is valid (e.g., no
     * loop, graph is not a multigraph). USE IT AT YOUR OWN RISK.
     *
     * @return pretty-print of this graph
     */
    public String pprint() {
        // Get alphabetical list of people, for prettiness
        List<String> people = new ArrayList<String>(this.hashmap.keySet());
        Collections.sort(people);

        // String writer is easier than appending tons of strings
        StringWriter writer = new StringWriter();

        // Print labels for matrix columns
        writer.append("   ");
        for (String person: people)
            writer.append(" " + person);
        writer.append("\n");

        // Print one line of social connections for each person
        for (String source: people) {
            writer.append(source);
            for (String target: people) {
                if (this.getNeighbors(source).contains(target))
                    writer.append("  X ");
                else
                    writer.append("    ");
            }
            writer.append("\n");

        }

        // Remove last newline so that multiple printlns don't have empty
        // lines in between
        String string = writer.toString();
        return string.substring(0, string.length() - 1);
    }

}
