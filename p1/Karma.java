///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  Reddit.java
// File:             Karma.java
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
 * Karma class stores the link, comment karmas of a particular user.
 * 
 * @author rachelzachariah
 *
 */
public class Karma {
	private int linkKarma; //link karma
	private int commentKarma; // comment karma

	public Karma() {
		this.linkKarma = 0; 
		this.commentKarma = 0; 
	}
/**
 * Increments the appropriate type of karma by two.
 * 
 * @param type Type of the post, i.e. link or comment.
 */
	public void upvote(PostType type) {
		if (type.equals(PostType.LINK)) {
			this.linkKarma = this.linkKarma + 2;
		}
		if (type.equals(PostType.COMMENT)){
			this.commentKarma = this.commentKarma + 2;
		}
		
	}

/**
 * Decreases the appropriate type of karma by two.
 * 
 * @param type Type of the post, i.e. link or comment.
 */
	public void downvote(PostType type) {
		if (type.equals(PostType.LINK)) {
			this.linkKarma = this.linkKarma - 1;
		}
		if (type.equals(PostType.COMMENT)){
			this.commentKarma = this.commentKarma - 1;
		}
	}

/**
 * Returns the link karma of the post.
 * 
 * @return the link karma of the post.
 */
	public int getLinkKarma() {
		return this.linkKarma;
	}
/**
 * Returns the comment karma of the post.
 * 
 * @return the comment karma of the post.
 */
	public int getCommentKarma() {
		return this.commentKarma;
	}
}
