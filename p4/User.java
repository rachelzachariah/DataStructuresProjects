///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FileSystemMain.java
// File:             User.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// 
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;

public class User {
	
	private String name; //name of the user.
	private ArrayList<SimpleFile> files;//list of files owned/created by user
	private ArrayList<SimpleFolder> folders;//list of folder owned by user.

	public User(String name) {
		if (name==null) throw new IllegalArgumentException();
		this.name = name;
		this.files = new ArrayList<SimpleFile>();
		this.folders = new ArrayList<SimpleFolder>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.name.equals(((User)obj).getName()))
			return true;
		return false;
	}

	//returns the name of the user.
	public String getName() {
		return this.name;
	}

	//returns the list of files owned by the user.
	public ArrayList<SimpleFile> getFiles() {
		return this.files;
	}

	//adds the file to the list of files owned by the user.
	public void addFile(SimpleFile newfile) {
		this.files.add(newfile);
	} 
	
	//removes the file from the list of owned files of the user.
	public boolean removeFile(SimpleFile rmFile){
		return this.files.remove(rmFile);
	}

	//returns the list of folders owned by the user.
	public ArrayList<SimpleFolder> getFolders() {
		return this.folders;
	}

	//adds the folder to the list of folders owned by the user.
	public void addFolder(SimpleFolder newFolder) {
		this.folders.add(newFolder);
	}

	//removes the folder from the list of folders owned by the user.
	public boolean removeFolder(SimpleFolder rmFolder){
		SimpleFolder curr;
		SimpleFile file;
		for (int i=0;i<this.folders.size();i++){
			curr = this.folders.get(i);
			if (curr.equals(rmFolder)){
				for (SimpleFolder f2:curr.getSubFolders())
					this.removeFolder(f2);
			}
		}
		for (int i=0;i<this.files.size();i++){
			file = this.files.get(i);
			if (file.getParent().equals(rmFolder)) this.removeFile(file);
		}
		return this.folders.remove(rmFolder);
	}
	
	//returns the string representation of the user object.
	@Override
	public String toString() {
		String retType = name + "\n";
		retType = retType + "Folders owned :\n";
		for(SimpleFolder preFolder : folders){
			retType = retType + "\t" + preFolder.getPath() + "/" + preFolder.getName() + "\n";
		}
		retType = retType + "\nFiles owned :\n"; 
		for(SimpleFile preFile : files){
			retType = retType + "\t" + preFile.getPath() + "/" + preFile.getName() + "." + preFile.getExtension().toString() + "\n";
		}
		return retType;
	}
	
	
	
}
