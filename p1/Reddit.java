///////////////////////////////////////////////////////////////////////////////
// Title:            p1
// Files:            
// Semester:         CS302 Spring 2015
//
// Author:           Rachel Alisha Zachariah	
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      -
//
// Online sources:   Concept of a flag variable: http://stackoverflow.com
//                   /questions/17402125/what-is-a-flag-variable/17402180#17402180 
//////////////////////////// 80 columns wide //////////////////////////////////


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator; //used to iterate through a list of posts.
import java.util.List; //store the information of the database
import java.util.Scanner;

/** Reddit, creates and uses an RedditDB to hold and process information.
 *  The user and post information is read from a text files and then the 
 *  program processes user commands.
 *  
 *  @author rachelzachariah
 */
public class Reddit {
	
    static RedditDB redditDB = new RedditDB();
    static User admin = redditDB.addUser("admin");
    static User loggedIn = null;
    
	public static void main(String[] args) {
		
		//Checks whether at least one command-line argument is given.
		if ( args.length < 1 ) {
            System.out.println( "Usage: java Reddit <FileNames>");
            return;
        }
		//Loads the data from the input files and uses it to construct the 
		//database.
        for ( int i=0; i < args.length; i++ ) {
            String filename = args[i];
            File file = new File( filename );
            try { 
                Scanner filescanner = new Scanner( file );
                String[] tokens = args[i].split("[.]");
                String name = tokens[0].toLowerCase(); 
                User user =redditDB.addUser(name);
                String line1 = filescanner.nextLine();
                String delims1 = "[,]";
                String[] tokens1 = line1.split(delims1);
                //Subscribes to subreddits.
                if (!tokens1.equals(null)){
                	for (int j=0; j< tokens1.length; j++ ){
                		user.subscribe(tokens1[j].trim());
                	}	   
                }
                //Loads posts.
                while(filescanner.hasNext()) {
                	String line2 = filescanner.nextLine();
                    String delims2 = "[,]";
                    String[] tokens2 = line2.split(delims2, 3);
                    Post newPost = user.addPost(tokens2[0].toLowerCase(), 
                    		PostType.valueOf(tokens2[1].replaceAll("[ ]+", "")), tokens2[2]);
                    user.like(newPost);
                }
                
                filescanner.close();
            } 
            //Checks whether the input files exists and is readable.
            catch ( FileNotFoundException fnf ) 
            {
                System.out.println("File "+ args[i] +" not found.");
            }
        }
        //Scans input commands.
        Scanner scanner = new Scanner( System.in );
        String input = null;
        prompt(loggedIn);
        //the following while loop generates the main menu.
        while (true) {
            input = scanner.nextLine().trim(); //holds input command.
            
            //Displays "Exiting to the real world..." and exit the simulation.
            if (input.equals("x")) {
            	System.out.println("Exiting to the real world...");
            	break;
            }	
            
            //Show summary of all users when the current user is "admin"; not
            //a valid command else.
            else if (input.equals("s")) {
            	if (loggedIn == null)
    				System.out.println("Invalid command!");
            	else{
	    			for (int i = 0; i<redditDB.getUsers().size(); i++) {
	    				System.out.println(redditDB.getUsers().get(i).getName() + 
	    						"\t" + redditDB.getUsers().get(i).getKarma().getLinkKarma() +
	    						"\t"+redditDB.getUsers().get(i).getKarma().getCommentKarma() );
	    			}
    			}
            }
            
            //Deletes the specified user if found and when the current user is admin.
            else if (input.split("[ ]+")[0].equals("d") & (input.split("[ ]+").length == 2)){
    			String name = (input.split("[ ]+")[1]).trim();
    			if (loggedIn == null){
    				System.out.println("Invalid command!");
    			}
    			else if (!loggedIn.equals(redditDB.findUser("admin"))){
    				System.out.println("Invalid command!");
    			}
    			else {
    				boolean del = redditDB.delUser(name);
    				if (del) 
    					System.out.println("User "+ name +" deleted.");
    				else 
    					System.out.println("User "+ name +" not found.");
    			}
    		}
            //Logs-in the user with the given name unless another user is already 
            //logged-in or the user does not exist.
            else if ((input.split("[ ]+")[0].equals("l")) & (input.split("[ ]+").length == 2)){
				if (loggedIn != null)
    				System.out.println("User "+ loggedIn.getName()+" already logged in.");
    			else {
    			    String name1 = (input.split("[ ]+")[1]);
				    if (redditDB.findUser(name1) == null) 
				    	System.out.println("User "+ name1 + " not found.");
				    else {
				    	loggedIn = redditDB.findUser(name1);
				    	System.out.println("User "+ name1 + " logged in.");
				    }        
    			}    
    		}
            // Logs-out the current user, unless no user is logged-in.
            else if (input.equals("l")){
    			if (loggedIn == null)
    				System.out.println("No user logged in.");
    			else {
    				System.out.println("User " + loggedIn.getName() +" logged out.");
    				loggedIn = null;
    			}
    		}
            //Displays the frontpage and prompts for submenu options for each 
            //post one by one.
            else if (input.equals("f")){
    			List<Post> frontPage = redditDB.getFrontpage(loggedIn);
    			Iterator<Post> itr = frontPage.iterator();
    			//if theere are no posts.
    			if (! itr.hasNext()) {
    				System.out.println("No more posts to display");
    			    System.out.println("Exiting to the main menu...");
    			}
    			//enter the submenu.
    			else{
    				System.out.println("Displaying the front page...");
    				while (itr.hasNext()){
	    				Post currPost = itr.next();
	    				System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
						prompt(loggedIn);
						int flag = subMenu(scanner, loggedIn, currPost, frontPage, itr);
						if (flag!= 0) //ie.submenu was exited
							break;
    				}
    			}
			}
            //Displays the frontpage for the given subreddit and prompts for 
            //submenu options for each post one by one.
            else if (input.split("[ ]+")[0].equals("r") & (input.split("[ ]+").length == 2)){
    			String subredditName = input.split("[ ]+")[1];
    			List<Post> frontPage = redditDB.getFrontpage(loggedIn, subredditName);
    			Iterator<Post> itr = frontPage.iterator();
    			System.out.println("Displaying/r/"+subredditName);
    			//if there are no posts.
    			if (! itr.hasNext()) {
    				System.out.println("No more posts to display");
    				System.out.println("Exiting to the main menu...");
    			}
    			//enter submenu
    			else{
    				while (itr.hasNext()){
	    				Post currPost = itr.next();
	    				System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
						prompt(loggedIn);
						int flag = subMenu(scanner, loggedIn, currPost, frontPage, itr);
						if (flag!= 0) //ie.submenu was exited
							break;
    				}
    			}
			}
            //Displays the posts of the given user and prompts for submenu options for
            //each post one by one.
            else if (input.split("[ ]+")[0].equals("u") & (input.split("[ ]+").length == 2)){
    			String name = input.split("[ ]")[1];
    			User user1 = redditDB.findUser(name);
    			if (user1!=null){
    				List<Post> userPosts = user1.getPosted();
    				Iterator<Post> itr = userPosts.iterator();
    				//if there are no posts.
        			if (! itr.hasNext()) {
        				System.out.println("No more posts to display");
        				System.out.println("Exiting to the main menu...");
        			}
        			//enter submenu.
        			else{
        				System.out.println("Displaying/u/"+name);
        				while (itr.hasNext()){
    	    				Post currPost = itr.next();
    	    				System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
    						prompt(loggedIn);
    						int flag = subMenu(scanner, loggedIn, currPost, userPosts, itr);
    						if (flag!= 0) //ie.submenu was exited "x"
    							break;
        				}
        			}
    			}
    		}
            //Displays error message if command was not valid.
    		else 
    			System.out.println("Invalid command!");
    		prompt(loggedIn);
        }
        return;
	}
	
	/**
	 * Handles the submenu options once a user is viewing a specific frontpage.
	 * 
	 * @param scanner reads the input commands from the user and passes it to 
	 * the method to be executed.
	 * @param loggedIn3 specifies which user if any is logged in.
	 * @param currPost specifies the current post which the submenu options
	 * may modify.
	 * @param frontPage the frontpage on which the user is currently.
	 * @param itr iterates through the front page list if needed.
	 * @return returns an integer flag variable that is "on" if and only if the
	 * user has tried to exit the sub menu.
	 * 
	 */

	private static int subMenu(Scanner scanner, User loggedIn3, Post currPost, 
			List<Post> frontPage, Iterator<Post> itr) {
	    int flag1 = 0; // tracks whether the submenu was exited (value 1) or not.
		while (true) {
        	String input = scanner.nextLine();
            if (input.equals("x")) {
            	System.out.println("Exiting to the main menu...");
            	flag1++;
            	break;
            }	
            else if (input.equals("a")){
            	if (loggedIn != null){
            		if (loggedIn.getDisliked().contains(currPost)){
            			loggedIn.undoDislike(currPost);
            		}
            		loggedIn.like(currPost);
	            	if (itr.hasNext()) {
	                	currPost = itr.next();
	                	System.out.println(currPost.getKarma() + "\t" +currPost.getTitle());
	        		}	
	            	else {
	                		System.out.println("No more posts to display");
	                		System.out.println("Exiting to the main menu...");
	                		break;
	                }	
            	}
            	else {
            		System.out.println("Log in to like post.");
            		System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
            	}
     
            }
            else if (input.equals("z")) {
            	if (loggedIn != null){
            		if (loggedIn.getLiked().contains(currPost)){
            			loggedIn.undoLike(currPost);
            		}
            		loggedIn.dislike(currPost);
            		if (itr.hasNext()) {
                    	currPost = itr.next();
                    	System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
            		}	
                	else {
                    		System.out.println("No more posts to display");
                    		System.out.println("Exiting to the main menu...");
                    		break;
                    }	
            	}
            	else {
            		System.out.println("Log in to dislike post.");
            		System.out.println(currPost.getKarma() +"\t"+ currPost.getTitle());
            	}
            }
            else if (input.equals("j")){
            	if (itr.hasNext()) {
                	currPost = itr.next();
                	System.out.println(currPost.getKarma() + "\t" +currPost.getTitle());
        		}	
            	else {
                		System.out.println("No more posts to display");
                		System.out.println("Exiting to the main menu...");
                		break;
                }	
            }
            else {
            	System.out.println("Invalid command!");
            	System.out.println(currPost.getKarma() + "\t"+ currPost.getTitle());
            }	
            prompt(loggedIn);
        }
		return flag1;
	}

	/**
	 * Generates the prompt depending on which user is logged in if any.
	 * 
	 * @param loggedIn2 specifies the user whose name is displayed in the 
	 * prompt.
	 */
	private static void prompt(User loggedIn2) {
		if (loggedIn2 != null)
            System.out.print("["+ loggedIn2.getName()+" @reddit"+ "]$" + " " );
    	else 
    		System.out.print("[anon@reddit]$"+ " ");
	}
}	
