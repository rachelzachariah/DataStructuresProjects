///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            p5
// Files:            SocialNetworkingApp.java, SocialGraph.java, 
//                   UndirectedGraph.java, GraphADT.java.
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class SocialNetworkingApp {

    /**
     * Returns a social network as defined in the file 'filename'.
     * See assignment handout on the expected file format.
     * @param filename filename of file containing social connection data
     * @return
     * @throws FileNotFoundException if file does not exist
     */
    public static SocialGraph loadConnections(String filename) throws FileNotFoundException {
        SocialGraph sGraph = new SocialGraph();
    	File file = new File( filename );
        @SuppressWarnings("resource")
		Scanner filescanner = new Scanner( file );
        
        while (filescanner.hasNext()){
        	String[] line = filescanner.nextLine().trim().split("[ ]+");
        	for (int i=0; i<line.length;i++)
        		sGraph.addVertex(line[i]); //creates users
        	for (int i=1;i<line.length;i++)
        		sGraph.addEdge(line[0], line[i]); //makes user's friend-list
        }
        return sGraph;
    }

    static Scanner stdin = new Scanner(System.in);
    static SocialGraph graph;
    static String prompt = ">> ";  // Command prompt

    /**
     * Access main menu options to login or exit the application.
     * 
     * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
     */
    public static void enterMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.print(prompt);
            String[] tokens = stdin.nextLine().split(" ");
            String cmd = tokens[0];
            String person = (tokens.length > 1 ? tokens[1] : null);

            switch(cmd) {
                case "login":
                    System.out.println("Logged in as " + person);
                    enterSubMenu(person);
                    System.out.println("Logged out");
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    /**
     * Access submenu options to view the social network from the perspective
     * of currUser. Assumes currUser exists in the network.
     * @param currUser
     */
    public static void enterSubMenu(String currUser) {

        // Define the set of commands that have no arguments or one argument
        String commands =
                "friends fof logout print\n" +
                "connection friend unfriend";
        Set<String> noArgCmds = new HashSet<String>
                (Arrays.asList(commands.split("\n")[0].split(" ")));
        Set<String> oneArgCmds = new HashSet<String>
                (Arrays.asList(commands.split("\n")[1].split(" ")));

        boolean logout = false;
        while (!logout) {
            System.out.print(prompt);

            // Read user commands
            String[] tokens = stdin.nextLine().split(" ");
            String cmd = tokens[0];
            String otherPerson = (tokens.length > 1 ? tokens[1] : null);

            // Reject erroneous commands
            // You are free to do additional error checking of user input, but
            // this isn't necessary to receive a full grade.
            if (tokens.length == 0) continue;
            if (!noArgCmds.contains(cmd) && !oneArgCmds.contains(cmd)) {
                System.out.println("Invalid command");
                continue;
            }
            if (oneArgCmds.contains(cmd) && otherPerson == null) {
                System.out.println("Did not specify person");
                continue;
            }

            switch(cmd) {

            case "connection": { //prints shortest path between currUser and otherPerson
                List<String> path = graph.getPathBetween(currUser, otherPerson);
                if (path==null)
                	System.out.println("You are not connected to "+otherPerson);
                else{
                	System.out.println(path.toString());
                }
                break;
            }

            case "friends": { //prints currUser's friends
                Set<String> neighbors = graph.getNeighbors(currUser);
                	if (neighbors.isEmpty())
                		System.out.println("You do not have any friends");
                	else{ 
                		System.out.println(neighbors.toString());
                	}
                break;
            }

            case "fof": { //prints currUser's friends of friends
                Set<String> fof = graph.friendsOfFriends(currUser);
                if (fof.isEmpty())
                	System.out.println("You do not have any friends of friends");
                else 
                	System.out.println(fof.toString());
                break;
            }

            case "friend": { //adds otherPerson as a friend of currUser
                if(!graph.addEdge(currUser, otherPerson))
                	System.out.println("You are already friends with "+otherPerson);
                else
                	System.out.println("You are now friends with "+otherPerson); 
                break;
            }

            case "unfriend": { //currUser unfriends otherPerson
                if(!graph.getNeighbors(currUser).contains(otherPerson))
                	System.out.println("You are already not friends with "+otherPerson);
                else
                	graph.removeEdge(currUser, otherPerson);        		
                break;
            }

            case "print": {
                // This command is left here for your debugging needs.
                // You may want to call graph.toString() or graph.pprint() here
            	// You are free to modify this or remove this command entirely.
            	//
                // YOU DO NOT NEED TO COMPLETE THIS COMMAND
                // THIS COMMAND WILL NOT BE PART OF GRADING
                break;
            }

            case "logout":
                logout = true;
                break;
            }  // End switch
        }
    }

    /**
     * Commandline interface for a social networking application.
     *
     * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java SocialNetworkingApp <filename>");
            return;
        }
        try {
            graph = loadConnections(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        enterMainMenu();

    }

}
