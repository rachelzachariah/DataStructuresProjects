import java.util.List;
import java.util.Scanner;



/**
 * Version control application. Implements the command line utility
 * for Version control.
 * @author
 *
 */
public class VersionControlApp {

	/* Scanner object on input stream. */
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * An enumeration of all possible commands for Version control system.
	 */
	private enum Cmd {
		AU, DU,	LI, QU, AR, DR, OR, LR, LO, SU, CO, CI, RC, VH, RE, LD, AD,
		ED, DD, VD, HE, UN
	}

	/**
	 * Displays the main menu help. 
	 */
	private static void displayMainMenu() {
		System.out.println("\t Main Menu Help \n" 
				+ "====================================\n"
				+ "au <username> : Registers as a new user \n"
				+ "du <username> : De-registers a existing user \n"
				+ "li <username> : To login \n"
				+ "qu : To exit \n"
				+"====================================\n");
	}

	/**
	 * Displays the user menu help. 
	 */
	private static void displayUserMenu() {
		System.out.println("\t User Menu Help \n" 
				+ "====================================\n"
				+ "ar <reponame> : To add a new repo \n"
				+ "dr <reponame> : To delete a repo \n"
				+ "or <reponame> : To open repo \n"
				+ "lr : To list repo \n"
				+ "lo : To logout \n"
				+ "====================================\n");
	}

	/**
	 * Displays the repo menu help. 
	 */
	private static void displayRepoMenu() {
		System.out.println("\t Repo Menu Help \n" 
				+ "====================================\n"
				+ "su <username> : To subcribe users to repo \n"
				+ "ci: To check in changes \n"
				+ "co: To check out changes \n"
				+ "rc: To review change \n"
				+ "vh: To get revision history \n"
				+ "re: To revert to previous version \n"
				+ "ld : To list documents \n"
				+ "ed <docname>: To edit doc \n"
				+ "ad <docname>: To add doc \n"
				+ "dd <docname>: To delete doc \n"
				+ "vd <docname>: To view doc \n"
				+ "qu : To quit \n" 
				+ "====================================\n");
	}

	/**
	 * Displays the user prompt for command.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered command (Max: 2 words).
	 */
	private static String[] prompt(String prompt) {
		System.out.print(prompt);
		String line = scnr.nextLine();
		String []words = line.trim().split(" ", 2);
		return words;
	}

	/**
	 * Displays the prompt for file content.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered content.
	 */
	private static String promptFileContent(String prompt) {
		System.out.println(prompt);
		String line = null;
		String content = "";
		while (!(line = scnr.nextLine()).equals("q")) {
			content += line + "\n";
		}
		return content;
	}

	/**
	 * Validates if the input has exactly 2 elements. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput2(String[] input) {
		if (input.length != 2) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input has exactly 1 element. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput1(String[] input) {
		if (input.length != 1) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the Cmd equivalent for a string command. 
	 * @param strCmd The string command.
	 * @return The Cmd equivalent.
	 */
	private static Cmd stringToCmd(String strCmd) {
		try {
			return Cmd.valueOf(strCmd.toUpperCase().trim());
		}
		catch (IllegalArgumentException e){
			return Cmd.UN;
		}
	}

	/**
	 * Handles add user. Checks if a user with name "username" already exists; 
	 * if exists the user is not registered. 
	 * @param username The user name.
	 * @return USER_ALREADY_EXISTS if the user already exists, SUCCESS otherwise.
	 */
	private static ErrorType handleAddUser(String username) {
		if (VersionControlDb.addUser(username) != null) {
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USERNAME_ALREADY_EXISTS;
		}
	}

	/**
	 * Handles delete user. Checks if a user with name "username" exists; if 
	 * does not exist nothing is done. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleDelUser(String username) {
		User user = VersionControlDb.findUser(username); 
		if (user == null) {
			return ErrorType.USER_NOT_FOUND;
		}
		else {
			VersionControlDb.delUser(user);
			return ErrorType.SUCCESS;
		}
	}

	/**
	 * Handles a user login. Checks if a user with name "username" exists; 
	 * if does not exist nothing is done; else the user is taken to the 
	 * user menu. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleLogin(String username) {
		User currUser = VersionControlDb.findUser(username);
		if (currUser != null) {
			System.out.println(ErrorType.SUCCESS);
			processUserMenu(currUser);
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USER_NOT_FOUND;
		}
	}

	/**
	 * Allows the admin of the current repository to revert the repository to 
	 * the previous version. 
	 * @param logInUser The logged in user.
	 * @param repo The current repo.
	 */
	private static void handleRev(User logInUser, Repo repo) {
		ErrorType et = repo.revert(logInUser);
		if (et.equals(ErrorType.ACCESS_DENIED)) System.out.println("ACCESS_DENIED");
		else if(et.equals(ErrorType.NO_OLDER_VERSION)) System.out.println("NO_OLDER_VERSION");
		else System.out.println("SUCCESS");
	}

	/**
	 * Allows the admin to review the current repository's queued checkins and 
	 * approve/deny those checkins. 
	 * @param logInUserThe logged in user.
	 * @param repo The current repo.
	 */
	private static void handleRevCheckIn(User logInUser, Repo repo) {
		if (repo.getCheckInCount()==0) System.out.println("NO_PENDING_CHECKINS");
		else if (!repo.getAdmin().equals(logInUser))
			System.out.println("ACCESS_DENIED");
		else{	
			int checkInCount = repo.getCheckInCount(); //number of check-Ins to be done.
			for (int i=0; i<checkInCount;i++){
				ChangeSet checkIn = repo.getNextCheckIn(logInUser);
				System.out.println(checkIn.toString());
				String[] command = prompt("Approve changes? Press y to accept: ");
				if (command[0].equals("y")) repo.approveCheckIn(logInUser, checkIn);
			}
			System.out.println("SUCCESS");
		}
	}

	/**
	 * Check-in or queues the pending changes in the local working copy to the 
	 * current repository for admin approval.
	 * @param logInUser The logged in user.
	 * @param repo The current repo.
	 */
	private static void handleCheckIn(User logInUser, Repo repo) {
		int changes = 0; //flag variable for whether pending changes exist. 
		for (String s:logInUser.getAllSubRepos()){
			if(logInUser.getPendingCheckIn(s)!=null){
				changes=1; // pending change exists
				Repo repo1 = VersionControlDb.findRepo(s);
				repo1.queueCheckIn(logInUser.getPendingCheckIn(s));
			}
		}
		if(changes==0) System.out.println("NO_LOCAL_CHANGES");
		else System.out.println("SUCCESS");
	}

	/**
	 * Displays an existing document in the logged-in user's local working copy of 
	 * the current repository. 
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 * @param currRepo The name of current repo.
	 */
	private static void handleViewDoc(User logInUser, String[] words,
			String currRepo) {
		String docName = words[1];
		Document doc = logInUser.getWorkingCopy(currRepo).getDoc(docName);
		if (doc==null) System.out.println("DOC_NOT_FOUND");
		else{
			System.out.println(doc.toString());
		}	
	}

	/**
	 * Deletes an document from the local working set of the current repository.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 * @param currRepo The name of current repo.
	 */
	private static void handleDelDoc(User logInUser, String[] words,
			String currRepo) {
		String docName = words[1];
		Document doc = logInUser.getWorkingCopy(currRepo).getDoc(docName);
		if (doc==null) System.out.println("DOC_NOT_FOUND");
		else{
			logInUser.addToPendingCheckIn(doc, Change.Type.DEL, currRepo);
			logInUser.getWorkingCopy(currRepo).delDoc(doc);
			System.out.println("SUCCESS");
		}
	}

	/**
	 * Adds a document to the logged-in user's local working copy of the current 
	 * repository.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 * @param currRepo The name of current repo.
	 */
	private static void handleAddDoc(User logInUser, String[] words,
			String currRepo) {
		String docName = words[1];
		Document doc1 = logInUser.getWorkingCopy(currRepo).getDoc(docName);
		if (doc1!=null) System.out.println("DOCNAME_ALREADY_EXISTS");
		else{
			String edit = promptFileContent("Enter the file content and press q to quit: ");
			Document doc = new Document(docName,edit,currRepo);
			logInUser.addToPendingCheckIn(doc, Change.Type.ADD, currRepo);
			logInUser.getWorkingCopy(currRepo).addDoc(doc);
			System.out.println("SUCCESS");
		}	
	}

	/**
	 * Edits an existing document in the logged-in user's local working copy of 
	 * the current repository.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 * @param currRepo The name of current repo.
	 */
	private static void handleEdDoc(User logInUser, String[] words,
			String currRepo) {
		String docName = words[1];
		Document doc1 = logInUser.getWorkingCopy(currRepo).getDoc(docName);
		if (doc1 == null) System.out.println("DOC_NOT_FOUND");
		else{
			String edit = promptFileContent("Enter the file content and press q to quit: ");
			Document doc = new Document(docName,edit,currRepo);
			logInUser.addToPendingCheckIn(doc, Change.Type.EDIT, currRepo);
			logInUser.getWorkingCopy(currRepo).getDoc(docName).setContent(edit);
			System.out.println("SUCCESS");
		}
	}
 
	/**
	 * Subscribes an existing user (with <username> ) to the current repository if 
	 * the logged-in user is the admin of the current repository.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 * @param currRepo The name of current repo.
	 * @param repo The current working repo.
	 */
	private static void handleSubsUser(User logInUser, String[] words, String currRepo, Repo repo) {
		if (!repo.getAdmin().equals(logInUser)) System.out.println("ACCESS_DENIED");
		else{ 
			User user = VersionControlDb.findUser(words[1]);
			if (user==null) System.out.println("USER_NOT_FOUND");
			else {
				user.subscribeRepo(currRepo);
				System.out.println("SUCCESS");
			}
		}
	}

	/**
	 * Opens an existing repository if the logged-in user is subscribed to the 
	 * repository. Opening a repository involves checking out a local working 
	 * copy for the repository if it doesn't already exist and taking the 
	 * logged-in user to the Repo Menu prompt.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 */
	private static void handleOpenRepo(User logInUser, String[] words) {
		String repoName =words[1];
		Repo repo = VersionControlDb.findRepo(repoName);
		if (repo== null) System.out.println("REPO_NOT_FOUND");
		else if (!logInUser.getAllSubRepos().contains(repoName)) 
			System.out.println("REPO_NOT_SUBSCRIBED");
		else {
			if(logInUser.getWorkingCopy(repoName)==null){
				logInUser.checkOut(repoName);
			}
			System.out.println("SUCCESS");
			processRepoMenu(logInUser,repoName);
		}
	}

	/**
	 * Lists the logged-in user's subscribed repositories.
	 * @param logInUser The logged in user.
	 */
	private static void handleListRepo(User logInUser) {
		List<String> subRepos=logInUser.getAllSubRepos();
		String str = "=================================== \n";
		str+= "Username: "+logInUser.getName()+"\n" 
				+ "-----------Repos------------------"+"\n" ;
		for (int i=0; i<subRepos.size();i++)
			str+= i+1+". "+ subRepos.get(i)+"\n";
		str+=subRepos.size()+" repo(s) subscribed."+"\n"
			+"====================================";
		System.out.println(str);
	}

	/**
	 * Deletes an existing repository from the database if the logged-in user
	 * is the admin for the repository. 
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 */
	private static void handleDelRepo(User logInUser, String[] words) {
		String repoName =words[1];
		Repo repo = (VersionControlDb.findRepo(repoName));
		if (repo ==null) System.out.println("REPO_NOT_FOUND");
		else if (!repo.getAdmin().equals(logInUser)) System.out.println("ACCESS_DENIED");
		else {
			VersionControlDb.delRepo(repo);
			System.out.println("SUCCESS");
		}
		
	}

	/**
	 * Adds a new repository with the logged-in user as its admin to the database
	 * and subscribes the logged-in user to the repository, if there exists no 
	 * repository with reponame.
	 * @param logInUser The logged in user.
	 * @param words The input commands.
	 */
	private static void handleAddRepo(User logInUser, String[] words) {
		String repoName =words[1];
		if (VersionControlDb.findRepo(repoName)!=null) 
			System.out.println("REPONAME_ALREADY_EXISTS");
		else{
			VersionControlDb.addRepo(repoName, logInUser);
			logInUser.subscribeRepo(repoName);
			System.out.println("SUCCESS");
		}
	}

	
	/**
	 * Processes the main menu commands.
	 * 
	 */
	public static void processMainMenu() {

		String mainPrompt = "[anon@root]: ";
		boolean execute = true;

		while (execute) {
			String[] words = prompt(mainPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AU:
				if (validateInput2(words)) {
					System.out.println(handleAddUser(words[1].trim()));
				}
				break;
			case DU:
				if (validateInput2(words)) {
					System.out.println(handleDelUser(words[1].trim())); 
				}
				break;
			case LI:
				if (validateInput2(words)) {
					System.out.println(handleLogin(words[1].trim()));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayMainMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * Processes the user menu commands for a logged in user.
	 * @param logInUser The logged in user.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processUserMenu(User logInUser) {

		if (logInUser == null) {
			throw new IllegalArgumentException();
		}

		String userPrompt = "[" + logInUser.getName() + "@root" + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(userPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AR:
				if (validateInput2(words)) {
					handleAddRepo(logInUser,words);
				}
				break;
			case DR:
				if (validateInput2(words)) {
					handleDelRepo(logInUser,words);
				}
				break;
			case LR:
				if (validateInput1(words)) {
					handleListRepo(logInUser);
				}
				break;
			case OR:
				if (validateInput2(words)) {
					handleOpenRepo(logInUser,words);
				}
				break;
			case LO: //Logs out the logged-in user and goes back to the main menu.
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			case HE: //Prints the user menu help.
				if (validateInput1(words)) {
					displayUserMenu();
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	
	/**
	 * Process the repo menu commands for a logged in user and current
	 * working repository.
	 * @param logInUser The logged in user. 
	 * @param currRepo The name of current working repo.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processRepoMenu(User logInUser, String currRepo) {

		if (logInUser  == null || currRepo == null) {
			throw new IllegalArgumentException();
		}

		String repoPrompt = "["+ logInUser.getName() + "@" + currRepo + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(repoPrompt);
			Cmd cmd = stringToCmd(words[0]);
			Repo repo = VersionControlDb.findRepo(currRepo);

			switch (cmd) {
			case SU:
				if (validateInput2(words)) {
					handleSubsUser(logInUser,words,currRepo, repo);
				}
				break;
			case LD: //returns a list of docs in the users current working copy.
				if (validateInput1(words)) {
					System.out.println(logInUser.getWorkingCopy(currRepo).toString());
				}
				break;
			case ED:
				if (validateInput2(words)) {
					handleEdDoc(logInUser,words,currRepo);
				}					
				break;
			case AD:
				if (validateInput2(words)) {
					handleAddDoc(logInUser, words,currRepo);
				}
				break;
			case DD:
				if (validateInput2(words)) {
					handleDelDoc(logInUser,words,currRepo);
				}
				break;
			case VD:
				if (validateInput2(words)) {
					handleViewDoc(logInUser,words,currRepo);
				}
				break;
			case CI:
				if (validateInput1(words)) {
					handleCheckIn(logInUser,repo);
				}
				break;
			case CO:
				//Check-out the latest version of the current repository 
				//into the working copy of the logged in user 
				if (validateInput1(words)) {
					logInUser.checkOut(currRepo);
					System.out.println("SUCCESS");
					}
				break;
			case RC:
				if (validateInput1(words)) {
					handleRevCheckIn(logInUser,repo);
				}
				break;
			case VH: //returns version history.
				if (validateInput1(words)) {
					System.out.println(repo.getVersionHistory());
				}
				break;
			case RE:	
				if (validateInput1(words)) {
					handleRev(logInUser,repo);
				}
				break;
			case HE: //Prints the repo menu help.
				if (validateInput1(words)) {
					displayRepoMenu();
				}
				break;
			case QU: //Quits the repo menu prompt.
				if (validateInput1(words)) {
					execute = false;
					System.out.println("SUCCESS");
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}
	
	/**
	 * The main method. Simulation starts here.
	 * @param args Unused
	 */
	public static void main(String []args) {
		try {
			processMainMenu(); 
		}
		// Any exception thrown by the simulation is caught here.
		catch (Exception e) {
			System.out.println(ErrorType.INTERNAL_ERROR);
			e.printStackTrace();
		}
		// Any clean up code goes here.
		finally {
			System.out.println("Quitting the simulation.");
		}
	}
}
