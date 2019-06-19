///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Reddit.java
// File:             Post.java
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
 * Represents a post in the Reddit application, stores a user, the appropriate 
 * subreddit, the type of the post, post title and remembers post karma.
 * 
 * @author rzachariah
 *
 */
public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;

	public Post(User user, String subreddit, PostType type, String title) {
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		this.karma = 0;
		
	}

	/**
	 * Increases the karma of the post by two, and the appropriate karma of the
	 * user by two.	
	 */
	public void upvote() {
		this.karma = this.karma + 2;
		getUser().getKarma().upvote(this.type);
	}
	/**
	 * Decreases the karma of the post by one, and the appropriate karma of the
	 * user by one.	
	 */
	public void downvote() {
		this.karma = this.karma - 1;
		getUser().getKarma().downvote(this.type);
	
	}

	/**
	 * Returns reference to the user who created the post.
	 * 
	 * @return returns reference to the user who created the post.
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Returns the subreddit of the post.
	 * 
	 * @return returns the subreddit of the post.
	 */
	public String getSubreddit() {
		return this.subreddit;
	}
	
	/**
	 * Retruns the type of this post ie.link or comment.
	 * 
	 * @return returns an enum of the PostType class corresponding to the 
	 * type of the post.
	 */

	public PostType getType() {
		return this.type;
	}

	/**
	 * Returns the title of the post.
	 * 
	 * @return returns the title of the post.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the karma of the post.
	 * 
	 * @return returns the karma of the post.
	 */
	public int getKarma() {
		return this.karma;
	}
}
