import java.util.ArrayList;

public class SimpleFile {
	private String name;
	private Extension extension;
	private String content;
	private User owner;
	private ArrayList<Access> allowedUsers;
	private String path;
	private SimpleFolder parent;
	
	public SimpleFile(String name, Extension extension, String path, String content, SimpleFolder parent, User owner) {
		if (name==null || extension==null || content==null || path==null || parent==null || owner==null )
			throw new IllegalArgumentException();
		this.name = name;
		this.extension = extension;
		this.content = content;
		this.owner = owner;
		this.allowedUsers = new ArrayList<Access>();
		this.path = path;
		this.parent = parent;
	}
	
	//returns the path variable.
	public String getPath(){
		return this.path;
	}

	//return the parent folder of this file.
	public SimpleFolder getParent() {
		return this.parent;
	}

	//returns the name of the file.
	public String getName() {
		return this.name;
	}

	//returns the extension of the file.
	public Extension getExtension() {
		return this.extension;
	}

	//returns the content of the file.
	public String getContent() {
		return this.content;
	}

	//returns the owner user of this file.
	public User getOwner() {
		return this.owner;
	}

	//returns the list of allowed user of this file.
	public ArrayList<Access> getAllowedUsers() {
		return this.allowedUsers;
	}

	//adds a new access(user+accesstype pair) to the list of allowed user.
	public void addAllowedUser(Access newAllowedUser) {
		if (newAllowedUser==null) throw new IllegalArgumentException();
		this.allowedUsers.add(newAllowedUser);
	}
	
	//adds a list of the accesses to the list of allowed users.
	public void addAllowedUsers(ArrayList<Access> newAllowedUser) {
		for (int i=0; i<newAllowedUser.size(); i++)
			this.allowedUsers.add(newAllowedUser.get(i));
	}
	
	
	// returns true if the user name is in allowedUsers.
	// Otherwise return false.
	public boolean containsAllowedUser(String name){
		for (Access a : this.allowedUsers){
			if (a.getUser().getName().equals(name))
				return true;
		}
		return false;
	}
	
	
	//removes the file for all users.
	//If the user is owner of the file or the admin or the user has 'w' access,
	//then it is removed for everybody.
	public boolean removeFile(User removeUsr){
		for (Access a : this.allowedUsers){
			if (a.getUser().equals(removeUsr)|| removeUsr.equals(owner))
				if (a.getAccessType() == 'w'||removeUsr.equals(owner)){
					owner.removeFile(this);
				}
			return true;
		}
		return false;
	}
	
	//returns the string representation of the file.
	@Override
	public String toString() {
		String retString = "";
		retString = name + "." + extension.name() + "\t" + owner.getName() + "\t" ;
		for(Access preAccess : allowedUsers){
			retString = retString + preAccess + " ";
		}
		retString = retString + "\t\"" + content + "\"";
		return retString;
	}
	
}
