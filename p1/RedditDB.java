///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Reddit.java
// File:             RedditDB.java
// Semester:         CS302 Spring 2015
//
// Author:           Rachel Alisha Zachariah	
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      -
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.List;
import java.util.ArrayList;
/**RedditDB class is a database for list of users, their posts and all 
 * related information available to the application.
 * 
 * @author rachelzachariah
 *
 */
public class RedditDB  {
	private List<User> users;

	public RedditDB(){
		this.users = new ArrayList<User>();
	}

/**Returns a copy of the users list in the RedditDB class.
 * 
 * @return a copy of the ussers list in this class.
 */
	public List<User> getUsers() {
		List<User> usersCopy = new ArrayList<User>();
		for (int i=0; i< this.users.size(); i++ ) {
			usersCopy.add(this.users.get(i));
		}
		return usersCopy;
	}

/**
 * Adds a user of the given name to the list of users.
 * 
 * @param name Name of the user to be added.
 * @return Returns a reference to the newly added user.
 */
	public User addUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		User newUser = new User(name);
		if (this.findUser(name) != null){
			return null;
		}
		else {
			this.users.add(newUser);
			return newUser;
		}
	}

/**
 * Finds a user of the given name in the users list and returns the user if 
 * found.
 * 
 * @param name Name of the user to be found.
 * @return Returns a reference to the user if found, else returns null.
 */
public User findUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		int number = 0; // keeps track of the user's position in the list for removal.
		if (this.users.isEmpty())
			return null;
		else 	
			while (!name.equals(this.users.get(number).getName())){
				number++;
				if (number >= this.users.size()) //ie.user not found
					break;
			}
			if (number<this.users.size()) //ie.user found
				return this.users.get(number);
			else
			    return null;		
	}
		
/**
 * Deletes a user of the given name if found and true, else returns false.
 * 
 * @param name Name of the user to be deleted.
 * @return true if the user was found and deleted, else returns false.
 */

	public boolean delUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		if (this.findUser(name) != null) {
			User user = this.findUser(name);
			//undo likes.
			for (int i=0; i < user.getLiked().size(); i++){
				user.getLiked().get(i).downvote();
				user.getLiked().get(i).downvote();
			}
			//undo dislikes.
			for (int i=0; i < user.getDisliked().size(); i++){
				user.getDisliked().get(i).upvote();
				user.getDisliked().get(i).downvote();
			}
			//delete user.
			for (int i=0; i < this.users.size(); i++){
				if (user.equals(this.users.get(i)))
					this.users.remove(i);
			}
			return true;
		}
		else
			return false;
	}

	/**
	 * Creates a list of posts that belong to the users subscribed subreddits 
	 * that unless posted by the user have not been liked/disliked by the
	 * user yet.  If the user is null this is all posts.
	 *   
	 * @param user User whose subscribed subreddits are checked to populate the
	 * list.
	 * @return List of all the posts that satisfy the given conditions.
	 */
	public List<Post> getFrontpage(User user) {
		List<Post> frontPagenull = new ArrayList<Post>();
		List<Post> frontPageuser = new ArrayList<Post>();
		// Creates a list of all posts.
		for (int i=0; i < this.users.size(); i++){
		    for (int k = 0; k < this.users.get(i).getPosted().size() ; k++) {
				frontPagenull.add(this.users.get(i).getPosted().get(k));
		    }
		}
		if (user == null) {
			return frontPagenull;
		}
		//if user is specified, gets only posts from the user's subreddits 
		//which unless posted by the user thselves, have not been liked or 
		//disliked yet.
		else {
			for (int i=0; i < frontPagenull.size(); i++){
				for (int j = 0; j < user.getSubscribed().size(); j++){
					if (user.getSubscribed().get(j).equals(
							frontPagenull.get(i).getSubreddit())){
						if(user.equals(frontPagenull.get(i).getUser())
								|| ((!user.getLiked().contains(
										frontPagenull.get(i)))
										&(!user.getDisliked().contains(
												frontPagenull.get(i))))){
							frontPageuser.add(frontPagenull.get(i));			    
						}
					}
				}
			}
			return frontPageuser;
		}
	}
		
/**
 * Returns a list of posts belonging to the specified subreddit if the user is
 * null, and if a user is specified then all such posts except those 
 * liked/disliked by the user unless created by the user themselves.
 * 
 * @param user Leave out posts liked/disliked by this user unless created by 
 * this user, if null return all subreddit posts.
 * 
 * @param subreddit Specifies which subreddit posts to be looking for.
 * @return returns the list of posts constructed under the above conditions.
 */

	public List<Post> getFrontpage(User user, String subreddit) {
		List<Post> frontpagesr = new ArrayList<Post>();
		List<Post> frontpageNullsr = new ArrayList<Post>();
		List<Post> frontpage = getFrontpage(null);
		//Creates a list of all subreddit posts.
		for (int i=0; i < frontpage.size(); i++) {
			if (frontpage.get(i).getSubreddit().equals(subreddit))
				frontpageNullsr.add(frontpage.get(i));
		}
		if (user == null)
			return frontpageNullsr;
		//if user is specified gets only the posts from the subreddit that
		//have not been liked/disliked by the user yet unless created by the 
		//user themselves.     
		else{
			for (int i=0; i < frontpageNullsr.size(); i++){
				if(user.equals(frontpageNullsr.get(i).getUser())
						|| ((!user.getLiked().contains(
								frontpageNullsr.get(i)))
								&(!user.getDisliked().contains(
										frontpageNullsr.get(i))))){
					frontpagesr.add(frontpageNullsr.get(i));
				}
			}
		return frontpagesr;
		}
	}
}
