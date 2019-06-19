///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FileSystemMain.java
// File:             Access.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Zachariah
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
// 
//
//////////////////////////// 80 columns wide //////////////////////////////////
public class Access {
	
	private User user;
	private char accessType;
	
	public Access(User user, char accessType) {
		if(user==null||(accessType!='r'&& accessType!='w'))
			throw new IllegalArgumentException();
		this.user = user;
		this.accessType = accessType;
	}

	public User getUser() {
		return this.user;
	}

	public char getAccessType() {
		return this.accessType;
	}

	public void setAccessType(char accessType) {
		if (accessType!='r'&& accessType!='w')
			throw new IllegalArgumentException();
		this.accessType = accessType;
	}
	
	@Override
	public String toString() {
		return (user.getName() + ":" + accessType);
	}
	
}
