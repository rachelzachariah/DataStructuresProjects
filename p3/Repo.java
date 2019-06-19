import java.util.ArrayList;
import java.util.List;


/**
 * Represents a repository which stores and tracks changes to a collection of 
 * documents.
 * @author
 *
 */
public class Repo {
	
	/* The current version of the repo. */
	private int version;
	
	/* The name of the repo. It's a unique identifier for a repository. */
	private final String repoName;
	
	/* The user who is the administrator of the repo. */
	private final User admin;
	
	/* The collection(list) of documents in the repo. */
	private final List<Document> docs;
	
	/* The check-ins queued by different users for admin approval. */
	private final QueueADT<ChangeSet> checkIns;
	
	/* The stack of copies of the repo at points when any check-in was applied. */
	private final StackADT<RepoCopy> versionRecords; 

	/**
	 * Constructs a repo object.
	 * @param admin The administrator for the repo.
	 * @param reponame The name of the repo.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public Repo(User admin, String repoName) {
		if(admin==null || repoName==null) throw new IllegalArgumentException();
		this.version=0;
		this.admin = admin;
		this.repoName =  repoName;
		this.docs =  new ArrayList<Document>();
		this.checkIns =  new SimpleQueue<ChangeSet>();
		this.versionRecords =  new SimpleStack<RepoCopy>();
		versionRecords.push(new RepoCopy(repoName,version,docs));
	}
	
	/**
	 * Return the name of the repo.
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.repoName;
	}
	
	/**
	 * Returns the user who is administrator for this repository.
	 * @return The admin user.
	 */
	public User getAdmin() {
		return this.admin;
	}
	
	/**
	 * Returns a copy of list of all documents in the repository.
	 * @return A list of documents.
	 */
	public List<Document> getDocuments() {
		return new ArrayList<Document>(this.docs);
	}
	
	/**
	 * Returns a document with a particular name within the repository.
	 * @param searchName The name of document to be searched.
	 * @return The document if found, null otherwise.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public Document getDocument(String searchName) {
    	if (searchName == null) {
			throw new IllegalArgumentException();
		}
    	
		for (Document d : this.docs) {
			if (d.getName().equals(searchName)) {
				return d;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the current version of the repository.
	 * @return The version of the repository.
	 */
	public int getVersion() {
		return this.version;
	}
	
	/**
	 * Returns the number of versions (or changes made) for this repository.
	 * @return The version count.
	 */
	public int getVersionCount() {
		return versionRecords.size();
	}
	
	/**
	 * Returns the history of changes made to the repository. 
	 * @return The string containing the history of changes.
	 */
	public String getVersionHistory() {
		StackADT<RepoCopy> tmpStack = new SimpleStack<RepoCopy>();
		RepoCopy repoVersion; 
		String str = "";
		while (!versionRecords.isEmpty()){
			try {
				 repoVersion = versionRecords.pop();
				 str+= repoVersion.toString()+"\n";
				 tmpStack.push(repoVersion);
			} catch (EmptyStackException e) {}	
		}
		while (!tmpStack.isEmpty()){
			try {
				versionRecords.push(tmpStack.pop());
			} catch (EmptyStackException e) {}
		}
		return str;
	}
	
	/**
	 * Returns the number of pending check-ins queued for approval.
	 * @return The count of changes.
	 */
	public int getCheckInCount() {
		return checkIns.size();
	}
	
	
	/**
	 * Queue a new check-in for admin approval.
	 * @param checkIn The check-in to be queued.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void queueCheckIn(ChangeSet checkIn) {
		if (checkIn==null) throw new IllegalArgumentException();
		checkIns.enqueue(checkIn);
	}
	
	/**
	 * Returns and removes the next check-in in the queue 
	 * if the requesting user is the administrator.
	 * @param requestingUser The user requesting for the change set.
	 * @return The checkin if the requestingUser is the admin and a checkin
	 * exists, null otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ChangeSet getNextCheckIn(User requestingUser) {
		if (requestingUser == null) throw new IllegalArgumentException();
		if (requestingUser!=admin) return null;
		try{
			return checkIns.dequeue();
		} catch(EmptyQueueException Ex){
			return null;
		}
	}
	
	/**
	 * Applies the changes contained in a particular checkIn and adds
	 * it to the repository if the requesting user is the administrator.
 	 * Also saves a copy of changed repository in the versionRecords.
	 * @param requestingUser The user requesting the approval.
	 * @param checkIn The checkIn to approve.
	 * @return ACCESS_DENIED if requestingUser is not the admin, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType approveCheckIn(User requestingUser, ChangeSet checkIn) {
		if (requestingUser==null || checkIn==null) throw new IllegalArgumentException();
		if (requestingUser!=admin) return ErrorType.ACCESS_DENIED;
		int changeCount = checkIn.getChangeCount();
		for (int i=0; i<changeCount;i++){
			Change change=checkIn.getNextChange();
			if (change.getType().equals(Change.Type.ADD)){
				this.docs.add(change.getDoc());
			}
			if (change.getType().equals(Change.Type.EDIT)){
				for (Document d : this.docs) {
					if (d.getName().equals(change.getDoc().getName())) {
						d=change.getDoc();
					}
				}
			}
			if (change.getType().equals(Change.Type.DEL)){
				for (int j =0; j< docs.size();j++) {
					if (docs.get(j).getName().equals(change.getDoc().getName())) {
						this.docs.remove(j);
					}
				}
			}
		}
		version++;
		RepoCopy repocopy = new RepoCopy(this.repoName,this.version,this.docs);
		versionRecords.push(repocopy);
		return ErrorType.SUCCESS;
	}
	
	/**
	 * Reverts the repository to the previous version if present version is
	 * not the oldest version and the requesting user is the administrator.
	 * @param requestingUser The user requesting the revert.
	 * @return ACCESS_DENIED if requestingUser is not the admin, 
	 * NO_OLDER_VERSION if the present version is the oldest version, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType revert(User requestingUser) {
		if (requestingUser == null) throw new IllegalArgumentException();
		if (requestingUser != admin) return ErrorType.ACCESS_DENIED;
		if (versionRecords.size()==1) return ErrorType.NO_OLDER_VERSION;
		try{
		versionRecords.pop();
		docs.clear();
		for (Document d: versionRecords.peek().getDocuments()){
			docs.add(d);
		}
		}catch(EmptyStackException Ex){
			return ErrorType.NO_OLDER_VERSION;
		}
		version = version-1;
		return ErrorType.SUCCESS;
	
	}
}
