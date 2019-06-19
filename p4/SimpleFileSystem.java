///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FileSystemMain.java
// File:             SimpleFileSystem.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// 
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("unused")
public class SimpleFileSystem {

	private SimpleFolder root;
	private ArrayList<User> users;
	private SimpleFolder currLoc;
	private User currUser;
	private User admin;
	
	//constructor
	public SimpleFileSystem(SimpleFolder _root, ArrayList<User> _users) {
		if (_root==null || _users==null) throw new IllegalArgumentException();
		this.root = _root;
		this.users = _users;
		this.currLoc = _root;
		this.currUser = _root.getOwner();
		this.admin = new User("admin");
	}

	// resets current user to admin and current location to root
	public void reset(){
		this.currUser = this.containsUser("admin");
		this.currLoc = this.root;
	}


	//gets currUser.
	public User getCurrUser() {
		return currUser;
	}

	//sets the current user to the user with name passed in the argument.
	public boolean setCurrentUser(String name){
		User tmpUser = new User(name);
		for (int i=0; i<this.users.size(); i++){
			if (this.users.get(i).equals(tmpUser)){
				currUser = this.users.get(i);
				currLoc = this.root;
				return true;
			}
		}
		return false;
	}


	//checks if the user is contained in the existing users list or not.
	public User containsUser(String name){
		User tmpUser = new User(name);
		for (int i=0; i<this.users.size(); i++){
			if (this.users.get(i).equals(tmpUser)){
				return this.users.get(i);
			}
		}
		return null;
	}
	
	
	//checks whether curr location contains any file/folder with name name = fname
	public boolean containsFileFolder(String fname){
		if (this.currLoc.getSubFolder(fname)!=null|| this.currLoc.getFile(fname)!=null)
			return true;
		return false;
	}
	


	//changes the current location. If successful returns true, false otherwise.
	public boolean moveLoc(String argument){
		SimpleFolder tmpLoc = currLoc;
		String delims = "[/]";
		String[] tokens = argument.toLowerCase().split(delims); 
		try{
			if (tokens[0].equals("")){              //absolute path case.
				if (tokens[1].equals("root")){      //absolute path must start from the root.
					tmpLoc=root;
					for (int i = 2;i<tokens.length;i++){    //move up one level
						if (tokens[i].equals("..")) {        
							if (tmpLoc.getParent()==null || 
									!tmpLoc.getParent().containsAllowedUser(currUser.getName())) 
								return false;
							else tmpLoc = tmpLoc.getParent(); 
						}
						else {                              //move down one level
							if (tmpLoc.getSubFolder(tokens[i])==null ||
									!tmpLoc.getSubFolder(tokens[i]).containsAllowedUser(currUser.getName())){
								return false;
							}
							tmpLoc = tmpLoc.getSubFolder(tokens[i]);
						}
					}
					currLoc = tmpLoc;
					return true;                       //successful move.
				}
				else return false;
			}
			else{                                 //relative path case.
				for (int i=0; i<tokens.length; i++){
					if (tokens[i].equals("..")) {     //move up one level
						if (tmpLoc.getParent()==null ||
								!tmpLoc.getParent().containsAllowedUser(currUser.getName())) 
							return false;
						else tmpLoc = tmpLoc.getParent();
					}
					else {                            //move down one level
						if (tmpLoc.getSubFolder(tokens[i])==null ||
								!tmpLoc.getSubFolder(tokens[i]).containsAllowedUser(currUser.getName()))
							return false;
						tmpLoc = tmpLoc.getSubFolder(tokens[i]);
					}
				}
				currLoc = tmpLoc;                  
				return true;                   //successful move
			}
		}catch (NullPointerException e){
			return false;                      //may be redundant but just in case!
		}
	}

	
	//returns the currentlocation.path + currentlocation.name.
	public String getPWD(){
		return ((currLoc.getPath().isEmpty()?"":(currLoc.getPath()+"/"))+currLoc.getName());
	}//return of getPWD method


	//deletes the folder/file identified by the 'name'
	public boolean remove(String name){
		SimpleFile file = this.currLoc.getFile(name);
		SimpleFolder folder = this.currLoc.getSubFolder(name);
		if (file!=null)  {
			return file.removeFile(currUser);
		}
		if (folder!= null) {
			return folder.removeFolder(currUser);
		}
		return false;
	}


	//Gives the access 'permission' of the file/folder fname to the user if the 
	//current user is the owner of the fname. If succeed, return true, otherwise false.
	public boolean addUser(String fname, String username, char permission){
		User user = new User(username);
		Access access = new Access(user,permission);
		SimpleFile file = this.currLoc.getFile(fname);
		SimpleFolder folder = this.currLoc.getSubFolder(fname);
		if (file!=null){
			if (file.getOwner().equals(currUser)){
				file.addAllowedUser(access);
				return true;
			}
		}
		if (folder!= null){
			if (folder.getOwner().equals(currUser)){
				folder.addAllowedUser(access);
				for (int i=0; i<folder.getFiles().size();i++){
					folder.getFiles().get(i).addAllowedUser(access);
				}
			return true;
			}
		}
		return false;
	}


	//displays the user info in the specified format.
	public boolean printUsersInfo(){
		if (currUser.equals(admin)){
			for (int i=0; i<this.users.size(); i++){
				System.out.println(this.users.get(i).toString());
			}
			return true;
		}
		return false;
	}

	//makes a new folder under the current folder with owner = current user.
	public void mkdir(String name){
		SimpleFolder folder = new SimpleFolder(name,this.getPWD(), currLoc, currUser);
		currLoc.addSubFolder(folder);
		currUser.addFolder(folder);
		Access access1 = new Access(currUser,'w');
		folder.addAllowedUser(access1);
		if (!currUser.equals(admin)){
			Access access2 = new Access(admin,'w');
			folder.addAllowedUser(access2);
		}
	}


	//makes a new file under the current folder with owner = current user.
	public void addFile(String filename, String fileContent){
		String delims = "[.]";
		String[] tokens = filename.toLowerCase().split(delims);
		SimpleFile file=null;
		if (tokens[1].equals("txt")){
			file = new SimpleFile(tokens[0],Extension.txt,this.getPWD(),fileContent,currLoc,currUser);
		}
		if (tokens[1].equals("doc")){
			file = new SimpleFile(tokens[0],Extension.doc,this.getPWD(),fileContent,currLoc,currUser);
		}
		if (tokens[1].equals("rtf")){
			file = new SimpleFile(tokens[0],Extension.rtf,this.getPWD(),fileContent,currLoc,currUser);
		}
		currLoc.addFile(file);
		currUser.addFile(file);
		Access access = new Access(currUser,'w');
		file.addAllowedUser(access);
		if (!currUser.equals(admin)){
			Access access2 = new Access(admin,'w');
			file.addAllowedUser(access2);
		}
	}


	//prints all the folders and files under the current location for which user has access.
	public void printAll(){
		for(SimpleFile f : currLoc.getFiles()){
			if(f.containsAllowedUser(currUser.getName()))
			{
				System.out.print(f.getName() + "." + f.getExtension().toString() + " : " + f.getOwner().getName() + " : ");
				for(int i =0; i<f.getAllowedUsers().size(); i++){
					Access a = f.getAllowedUsers().get(i);
					System.out.print("("+a.getUser().getName() + "," + a.getAccessType() + ")");
					if(i<f.getAllowedUsers().size()-1){
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
		for(SimpleFolder f: currLoc.getSubFolders()){
			if(f.containsAllowedUser(currUser.getName()))
			{
				System.out.print(f.getName() + " : " + f.getOwner().getName() + " : ");
				for(int i =0; i<f.getAllowedUsers().size(); i++){
					Access a = f.getAllowedUsers().get(i);
					System.out.print("("+a.getUser().getName() + "," + a.getAccessType() + ")");
					if(i<f.getAllowedUsers().size()-1){
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
		

	}

}
