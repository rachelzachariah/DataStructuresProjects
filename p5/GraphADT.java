///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp.java
// File:             GraphADT.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.Set;

public interface GraphADT<V> {

   boolean addVertex(V vertex);
   boolean addEdge(V v1, V v2);
   void removeEdge(V v1, V v2);
   Set<V> getNeighbors(V vertex);
   Set<V> getAllVertices();

}
