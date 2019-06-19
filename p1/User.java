///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Reddit.java
// File:             User.java
// Semester:         CS302 Spring 2015
//
// Author:           Rachel Alisha Zachariah	
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// Lab Section:      -
//
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * Represents a user of the reddit application. It stores a username, karma, and 
 * lists of liked, disliked posts and subscribed subreddits of the user. 
 * 
 * @author rachelzachariah
 */
import java.util.List;
import java.util.ArrayList;

public class User {
	final private String name;
	final private Karma karma;
	private List<String> subscribed;
	private List<Post> posted;
	private List<Post> liked;
	private List<Post> disliked;

	public User(String name) {
		this.name = name;
		this.karma = new Karma();
		this.subscribed = new ArrayList<String>();
		this.posted = new ArrayList<Post>();
		this.liked = new ArrayList<Post>();
		this.disliked = new ArrayList<Post>();
	}

	/**
	 * Returns the name of the user.
	 * 
	 * @return returns the name of the user.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns a reference to the Karma class of the user.
	 * 
	 * @return returns the Karma of the user.
	 */
	public Karma getKarma() {
		return this.karma;
	}

	/**
	 * Returns a copy of the list of the user's subscribed subreddits.
	 * 
	 * @return returns a list of the user's subscribed subreddits.
	 * 
	 */
	public List<String> getSubscribed() {
		List<String> subsCopy = new ArrayList<String>();
		for (int i = 0; i < subscribed.size(); i++){
			subsCopy.add(subscribed.get(i));
		}
		return subsCopy;
	}

	/**
	 * Returns a copy of the list of posts posted by the user.
	 * 
	 * @return returns a list of posts posted by the user.
	 * 
	 */
	public List<Post> getPosted() {
		List<Post> postCopy = new ArrayList<Post>();
		for (int i = 0; i < posted.size(); i++){
			postCopy.add(posted.get(i));
		}
		return postCopy;
	}
	/**
	 * Returns a copy of the list of posts liked by the user.
	 * 
	 * @return returns a list of posts liked by the user.
	 * 
	 */
	public List<Post> getLiked() {
		List<Post> likedCopy = new ArrayList<Post>();
		for (int i = 0; i < liked.size(); i++){
			likedCopy.add(liked.get(i));
		}
		return likedCopy;
	}
	/**
	 * Returns a copy of the list of posts disliked by the user.
	 * 
	 * @return returns a list of posts disliked by the user.
	 * 
	 */

	public List<Post> getDisliked() {
		List<Post> dislikedCopy = new ArrayList<Post>();
		for (int i = 0; i < disliked.size(); i++){
			dislikedCopy.add(disliked.get(i));
		}
		return dislikedCopy;
	}
	
	/**
	 * Subscribes the user to the specified subreddit,if not already 
	 * subscribed else unsubcribes.
	 * 
	 * @param subreddit specifies the subreddit to which the user 
	 * subscribes/unsubscribes.
	 */

	public void subscribe(String subreddit) {
		if (this.subscribed.contains(subreddit)) {
			this.unsubscribe(subreddit);
		}
		else {
			subscribed.add(subreddit);
		}
	}

	/**
	 * Unsubscribes the user from the specified subreddit, if not already 
	 * unsubscribed else does nothing.
	 * 
	 * @param subreddit specifies the subreddit to which the user unsubscribes.
	 */
	public void unsubscribe(String subreddit) {
		int remove = -1;
		for (int i=0 ; i < this.subscribed.size(); i++) {
			if (subreddit.equals(subscribed.get(i))) {
				remove = i;
				break;
			}
		if (remove >= 0)
			subscribed.remove(remove);
		 }
	}

	/**
	 * Creates a post of the given type, subreddit, title and adds it to the 
	 * list of user's posts and returns the added post.
	 * 
	 * @param subreddit subreddit of the given post.
	 * @param type type of the given post eg. link or comment.
	 * @param title title of the given post.
	 * @return returns the post that was added.
	 */
	public Post addPost(String subreddit, PostType type, String title) {
		Post newPost = new Post(this, subreddit, type, title);
		posted.add(newPost);
		return newPost;
	}
	
	/**
	 * Upvotes the post and add it to the List of liked posts if not already 
	 * liked; else undoes the like.If the post is currently disliked by the 
	 * user, the dislike is undone.
	 * 
	 * @param post specified the post that was liked.
	 */

	public void like(Post post) {
		if (! liked.contains(post)) { 
			if (!disliked.contains(post)){
				post.upvote();
				liked.add(post);
			}
			if (disliked.contains(post)){
				this.undoDislike(post);
				post.upvote();
			}
		}
	    else 
	    	this.undoLike(post);
	}

	/**
	 * Removes the post from the list of liked posts and updates its karma 
	 * appropriately.
	 * 
	 * @param post specifies the post to be removed.
	 * 
	 */
	public void undoLike(Post post) {
		if (liked.contains(post)) {
			int count = 0;
			for (int i=0 ; i < liked.size() ; i++) {
				if (! post.equals(liked.get(i))) 
					count ++;
				else {
					liked.remove(count);
					post.downvote();
					post.downvote();
					break;
				}
		    }
		}	
	}
		
	/**
	 * Downvotes the post and adds it to the List of disliked posts if not 
	 * already disliked; else undoes the dislike. If the post is currently 
	 * liked by the user, the like is undone.
	 * 
	 * @param post specifies the post to be disliked.
	 * 
	 */

	public void dislike(Post post) { 
		if (! disliked.contains(post)) {
			if (!liked.contains(post)){
				post.downvote();
				disliked.add(post);
			}
			if (liked.contains(post)) {
				this.undoLike(post);
				post.downvote();
			}	
		}
		else 
			this.undoDislike(post);
			
	}
	
	/**
	 * Removes the post from the list of disliked posts and updates its karma 
	 * appropriately.
	 * 
	 * @param post specifies the post to be removed.
	 */

	public void undoDislike(Post post) {
		if(disliked.contains(post)){
			int count = 0;
			for (int i=0 ; i < disliked.size() ; i++) {
				if (! post.equals(disliked.get(i))) 
					count ++;
				else {
				    post.upvote();
				    post.downvote();
					disliked.remove(count);
					break;
				}	
		    }
	    }
    }
}	
