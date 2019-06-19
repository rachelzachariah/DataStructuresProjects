///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            p4
// Files:            FileSystemMain.java, SimpleFileSystem.java, SimpleFolder.java
//                   SimpleFile.java, User.java, Access.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class FileSystemMain {

	private static final Scanner scnr = new Scanner(System.in);
	
	private static User admin = new User("admin");
	
	private static User login = admin; //keeps track of the current user for the purpose of the prompt methods.
	
	private static SimpleFileSystem fileSystem; 
	
	/**
	 * Reads input file and accordingly modifies the SimpleFileSystem "fileSystem".
	 * @param args Input arguments for files to be read.
	 * @param execute Boolean in the main program which controls program termination.
	 * @return Boolean that controls whether the main program should continue to execute or quit.
	 */
	private static boolean inputFile(String[] args,boolean execute){
				
		if (args.length != 1) {
			System.out.println("Usage: java FileSystemMain FileName");
			execute = false;
		}
		
		else{
			String filename = args[0];
            File file = new File( filename );
            try 
            { 
                Scanner filescanner = new Scanner( file );
                String line1 = filescanner.nextLine();
                String[] line2 = filescanner.nextLine().trim().split(",");
                
                ArrayList<User> users = new ArrayList<User>();
                users.add(admin);
                for (int i=0; i<line2.length; i++){
                	users.add(new User(line2[i].trim().toLowerCase()));
                }
                
                SimpleFolder root = new SimpleFolder(line1,"",null,admin);
                for (int j=0;j<users.size();j++){
    				Access a = new Access(users.get(j),'r');
    				root.addAllowedUser(a);
    			}
                
                fileSystem = new SimpleFileSystem(root,users);
                
                while ( filescanner.hasNext() ) {
                	String[] newLine = filescanner.nextLine().trim().split("/");
                	for (int i=1; i<newLine.length-1;i++){
                		String fname = newLine[i].toLowerCase();
                		if (fileSystem.containsFileFolder(fname)){
                			fileSystem.moveLoc(fname);
                		}
                		else{
                			fileSystem.mkdir(fname); 
                			for (int j=1;j<users.size();j++){
                				fileSystem.addUser(fname, users.get(j).getName(), 'r');
                			}
                		}
                	}
                	String[] leaf = newLine[newLine.length-1].split(" ",2);
                	String[] leaf1 = leaf[0].split("[.]"); 
                	if (leaf1.length==1){
                		String name = leaf[0].toLowerCase();
                		fileSystem.mkdir(name);
                		for (int j=1;j<users.size();j++){
            				fileSystem.addUser(name, users.get(j).getName(), 'r');
            			}
                	}
                	else{
                		String name = leaf[0].toLowerCase();
                		fileSystem.addFile(name,leaf[1].toLowerCase()); 
                		for (int j=0;j<users.size();j++){
            				fileSystem.addUser(name, users.get(j).getName(), 'r');
            			}
                }
                	fileSystem.reset();
                }
                filescanner.close();
            } 
            catch ( FileNotFoundException fnf ) 
            { }
		}
		return execute;
	}

	/**
	 * Generates the prompt.
	 * @return String array with user input.
	 */
	private static String[] prompt() {
		String prompt;
		if (login==null) prompt = "<username>@CS367$ ";
		else prompt = login.getName()+"@CS367$ ";
		System.out.print(prompt);
		String line = scnr.nextLine();
		String []words = line.trim().split("[ ]+", 3);
		return words;
	}
	
	/**
	 * Generates a message to be displayed to the user.
	 * @param msg Message to be displayed.
	 */
	private static void prompt(String msg){
		String prompt;
		if (login==null) prompt = "<username>@CS367$ "+msg;
		else prompt = login.getName()+"@CS367$ "+msg;
		System.out.println(prompt);
	}
	
	/**
	 * Checks if input is of a certain length or not.
	 * @param input the string array to be validated.
	 * @param n required length of the input.
	 * @return returns a boolean indicating whether the input had the 
	 * required length
	 */
	private static boolean validateInput(String[] input, int n) {
		if (input.length != n) {
			System.out.println("Invalid command.");
			return false;
		}
		return true;
	}

	/**
	 * Generates the menu for the program.
	 * @param args String array with the names of input text files.
	 */
	public static void processMenu(String[] args){
		
		boolean execute  = true;
		
		execute = inputFile(args,execute);
		
		while (execute){
			
			String[] commands = prompt();
			
			if(commands[0].equals("")){}
				
			else if (commands[0].equals("reset")){
				if (validateInput(commands,1)){
					fileSystem.reset(); 
					login = admin;
					prompt("Reset done");
				}
			}
			else if (commands[0].equals("pwd")){
				if (validateInput(commands,1)){
					System.out.println(fileSystem.getPWD());
				}

			}
			else if (commands[0].equals("ls")){
				if (validateInput(commands,1)){
					fileSystem.printAll();
				}
	
			}	
			else if (commands[0].equals("u")){
				if (validateInput(commands,2)){
					String name= commands[1].toLowerCase();
					User currUser = fileSystem.containsUser(name);
					if (currUser!=null){
						fileSystem.reset();
						fileSystem.setCurrentUser(name);
						login = currUser;
					}
					else{
						prompt("user <"+commands[1]+"> does not exist.");
					}
				}
				
			}
			else if (commands[0].equals("uinfo")){
				if (validateInput(commands,1)){
					if (admin.equals(login)){
						fileSystem.printUsersInfo();
					}
					else{
						prompt("Insufficient privileges");
					}
				}
				
			}	
			else if (commands[0].equals("cd")){
				if (validateInput(commands,2)){
					String loc = commands[1].toLowerCase();
					if(!fileSystem.moveLoc(loc)){
						prompt("Invalid location passed");
					}
				}
				
			}	
			else if (commands[0].equals("rm")){
				if (validateInput(commands,2)){
					boolean deleted = false; //whether the file/folder has been deleted.
					String fname= commands[1].toLowerCase();
					if (fileSystem.containsFileFolder(fname)){
						deleted = fileSystem.remove(fname);
						if (deleted==false) prompt("Insufficient privilege");
						else prompt(commands[1]+" removed");
					}
					else{
						prompt("Invalid name");
					}
				}
				
			}	
			else if (commands[0].equals("mkdir")){
				if (validateInput(commands,2)){
					fileSystem.mkdir(commands[1].toLowerCase());
					prompt(commands[1]+" added");
				}
				
			}	
			else if (commands[0].equals("mkfile")){
				if (commands.length==2){
					String fname = commands[1].toLowerCase();
					fileSystem.addFile(fname, "");
					prompt(commands[1]+" added");
				}
				else if (commands.length==3){
					String fname = commands[1].toLowerCase();
					String filecontent = commands[2].toLowerCase();
					fileSystem.addFile(fname, filecontent);
					prompt(commands[1]+" added");
				}
			}	
			else if (commands[0].equals("sh")){
				if (validateInput(commands,3)){
					String fname = commands[1].toLowerCase();
					String[] commands1 = commands[2].toLowerCase().split(" "); // the last string of 
					                                      //commands must be split in to its two arguments.
					if (commands1.length!=2){
						prompt("Three arguments needed");
					}
					else if((!commands1[1].equals("r"))&&(!commands1[1].equals("w"))){
						prompt("Invalid permission type");
					}
					else if(fileSystem.containsUser(commands1[0])==null){
						prompt("Invalid user");
					}
					else if(!fileSystem.containsFileFolder(fname)){
						prompt("Invalid file/folder name");
					}
					else if (fileSystem.addUser(fname, commands1[0],commands1[1].charAt(0))){
						prompt("Privilege granted");
					}
					else{
						prompt("Insufficient privilege");
					}
				}
				else{
					prompt("Three arguments needed");
				}
			}	
			else if (commands[0].equals("x")){
				execute = false;
			}	
			else{
				prompt("invalid command");
			}
		}
	}
	public static void main(String[] args) {
		
		processMenu(args);
		
	}	
	
	
}
