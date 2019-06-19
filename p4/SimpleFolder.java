///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FileSystemMain.java
// File:             SimpleFolder.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// 
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;

public class SimpleFolder {

	private String name;
	private String path;//absolute path of the folder.
	private SimpleFolder parent;
	private User owner;
	private ArrayList<SimpleFolder> subFolders;
	private ArrayList<SimpleFile> files;
	private ArrayList<Access> allowedUsers;

	public SimpleFolder(String name, String path, SimpleFolder parent, User owner) {
		if(name==null||path==null||owner==null) 
			throw new IllegalArgumentException();
		this.name = name;
		this.path = path;
		this.parent = parent;
		this.owner = owner;
		this.subFolders = new ArrayList<SimpleFolder>();
		this.files = new ArrayList<SimpleFile>();
		this.allowedUsers = new ArrayList<Access>();
	}
	
	
	//checks if user - "name" is allowed to access this folder or not. 
	//If yes, return true, otherwise false.
	public boolean containsAllowedUser(String name){
		for (Access a : this.allowedUsers){
			if (a.getUser().getName().equals(name))
				return true;
		}
		return false;
	}

	//checks if this folder contains the child folder identified by 'fname'.
	// If it does contain then it returns the folder otherwise returns null.
	public SimpleFolder getSubFolder(String fname){
		for (SimpleFolder f:this.subFolders){
			if (f.getName().equals(fname))
				return f;
		}
		return null;
	}


	//checks if this folder contains the child file identified by "fname".
	// If it does contain, return File otherwise null.
	public SimpleFile getFile(String fname){
		for (SimpleFile f:this.files){
			if (f.getName().equals(fname))
				return f;
		}
		return null;
	}


	//returns the owner of the folder.
	public User getOwner() {
		return this.owner;
	}

	//returns the name of the folder.
	public String getName() {
		return this.name;
	}

	//returns the path of this folder.
	public String getPath() {
		return this.path;
	}

	//returns the Parent folder of this folder.
	public SimpleFolder getParent() {
		return this.parent;
	}

	//returns the list of all folders contained in this folder.
	public ArrayList<SimpleFolder> getSubFolders() {
		return this.subFolders;
	}

	//adds a folder into this folder.
	public void addSubFolder(SimpleFolder subFolder) {
		this.subFolders.add(subFolder);
	}

	//adds a folder into this folder.
	public void addSubFolder(String name, SimpleFolder parent, User owner){
		SimpleFolder subFolder = new SimpleFolder(name,this.path+"/name",parent,owner);
		this.subFolders.add(subFolder);
	}

	//returns the list of files contained in this folder.
	public ArrayList<SimpleFile> getFiles() {
		return this.files;
	}

	//add the file to the list of files contained in this folder.
	public void addFile(SimpleFile file) {
		this.files.add(file);
	}

	//returns the list of allowed user to this folder.
	public ArrayList<Access> getAllowedUsers() {
		return this.allowedUsers;
	}

	//adds another user to the list of allowed user of this folder.
	public void  addAllowedUser(Access allowedUser) {
		this.allowedUsers.add(allowedUser);
	}

	//adds a list of allowed user to this folder.
	public void addAllowedUser(ArrayList<Access> allowedUser) {
		for (Access a:allowedUser) addAllowedUser(a);
	}

	//If the user is owner of this folder or the user is admin or the user has 'w' privilege
	//, then delete this folder along with all its content.
	public boolean removeFolder(User removeUsr){
		for (Access a : this.allowedUsers){
			if (a.getUser().equals(removeUsr)|| removeUsr.equals(owner)){
				if (a.getAccessType() == 'w'||removeUsr.equals(owner)){
					owner.removeFolder(this);
					this.parent.getSubFolders().remove(this);
					return true;
				}
			}
		}
		return false;
	}


	//returns the string representation of the Folder object.
	@Override
	public String toString() {
		String retString = "";
		retString = path + "/" + name + "\t" + owner.getName() + "\t";
		for(Access preAccess: allowedUsers){
			retString = retString + preAccess + " ";
		}

		retString = retString + "\nFILES:\n";

		for(int i=0;i<files.size();i++){
			retString = retString + files.get(i);
			if(i != files.size()-1)
				retString = retString + "\n";

		}				
		return retString;
	}


}
