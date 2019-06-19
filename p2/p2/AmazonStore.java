///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            p2
// Files:            AmazonStore.java, DLinkedList.java, Product.java, User.java, 
//                   InsufficientCreditException.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



public class AmazonStore {

	//Store record of users and products
	private static ListADT<Product> products = new DLinkedList<Product>();
	private static ListADT<User> users = new DLinkedList<User>();
	private static User currentUser = null;//current user logged in

	//scanner for console input
	public static final Scanner stdin= new Scanner(System.in);


	//main method
	public static void main(String args[]) {


		//Populate the two lists using the input files: Products.txt User1.txt User2.txt ... UserN.txt
		if (args.length < 2) {
			System.out.println("Usage: java AmazonStore [PRODUCT_FILE] [USER1_FILE] [USER2_FILE] ...");
			System.exit(0);
		}

		//load store products
		loadProducts(args[0]);

		//load users one file at a time
		for(int i=1; i<args.length; i++)
			loadUser(args[i]);

		//User Input for login
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter username : ");
			String username = stdin.nextLine();
			System.out.print("Enter password : ");
			String passwd = stdin.nextLine();

			if(login(username, passwd)!=null)
			{
				//generate random items in stock based on this user's wish list
				ListADT<Product> inStock=currentUser.generateStock();
				//show user menu
				userMenu(inStock);
			}
			else
				System.out.println("Incorrect username or password");

			System.out.println("Enter 'exit' to exit program or anything else to go back to login");
			if(stdin.nextLine().equals("exit"))
				done = true;
		}

	}

	/**
	 * Tries to login for the given credentials. Updates the currentUser if successful login
	 * 
	 * @param username name of user
	 * @param passwd password of user
	 * @returns the currentUser 
	 */
	public static User login(String username, String passwd){
		for (int i = 0; i<users.size(); i++){
			if (users.get(i).checkLogin(username, passwd) == true){
				currentUser = users.get(i);
				return currentUser;
			}
		}
		return null;
	}

	/**
	 * Reads the specified file to create and load products into the store.
	 * Every line in the file has the format: <NAME>#<CATEGORY>#<PRICE>#<RATING>
	 * Create new products based on the attributes specified in each line and insert them into the products list
	 * Order of products list should be the same as the products in the file
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadProducts(String fileName){
		File file = new File( fileName );
        try { 
            Scanner filescanner = new Scanner( file );
            while(filescanner.hasNext()) { //uses each line of the file to create a new product
            	String line = filescanner.nextLine();
                String delims = "[#]";
                String[] tokens = line.split(delims);
                String name = tokens[0];
                String categ = tokens[1];
                int price = Integer.parseInt(tokens[2]);
                float rating = Float.parseFloat(tokens[3]);
                Product newProduct = new Product(name, categ, price, rating);
                products.add(newProduct);
            }
            filescanner.close();
       } catch ( FileNotFoundException fnf ) 
        {
            System.out.println("Error: Cannot access file");
        }
	}

	/**
	 * Reads the specified file to create and load a user into the store.
	 * The first line in the file has the format:<NAME>#<PASSWORD>#<CREDIT>
	 * Every other line after that is a name of a product in the user's wishlist, format:<NAME>
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadUser(String fileName){
		File file = new File( fileName );
		try { 
            Scanner filescanner = new Scanner( file );
            String[] tokens1 = filescanner.nextLine().split("#"); //first line specifies the user to be created
            User newUser = new User(tokens1[0], tokens1[1],Integer.parseInt(tokens1[2])); 
            users.add(newUser);
            while(filescanner.hasNext()) {// uses the remaining lines of the file to populate wishlist with products
            	String line = filescanner.nextLine();
            	for (int i=0; i<products.size();i++){
            		if (products.get(i).getName().equals(line)){
            			newUser.addToWishList(products.get(i));
            		}
            	}
            }
            filescanner.close();
       } catch ( FileNotFoundException fnf ) 
        {
            System.out.println("Error: Cannot access file");
        }
	}

	/**
	 * See sample outputs
     * Prints the entire store inventory formatted by category
     * The input text file for products is already grouped by category, use the same order as given in the text file 
     * format:
     * <CATEGORY1>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * 
     * <CATEGORY2>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public static void printByCategory(){
		String category = products.get(0).getCategory();
		System.out.println(category);
		for (int i=0; i< products.size(); i++  ){
			if (products.get(i).getCategory().equals(category))
				System.out.println(products.get(i).toString());
			else{ 
				category = products.get(i).getCategory(); 
				System.out.println(" ");
				System.out.println(category+":"); //prints a new category in a new line
				System.out.println(products.get(i).toString());
			}	
		}
	}

	
	/**
	 * Interacts with the user by processing commands
	 * 
	 * @param inStock list of products that are in stock
	 */
	public static void userMenu(ListADT<Product> inStock){

		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter option : ");
			String input = stdin.nextLine();

			//only do something if the user enters at least one character
			if (input.length() > 0) 
			{
				String[] commands = input.split(":");//split on colon, because names have spaces in them
				if(commands[0].length()>1)
				{
					System.out.println("Invalid Command");
					continue;
				}
				switch(commands[0].charAt(0)){
				case 'v':
					if (commands.length>1){
						String command = commands[1].trim();
						if (command.equals("all")){
							printByCategory();
						}
						if (command.equals("wishlist")){
							currentUser.printWishList(System.out);
						}
						if (command.equals("instock")){
							for (int i=0; i< inStock.size(); i++){
								System.out.println(inStock.get(i).toString());
							}
						}
					}
					else {
						System.out.println("Invalid Command");
					}	
					break;

				case 's':
					if (commands.length>1){
						String string = commands[1].trim();
						for (int i = 0; i<products.size();i++){
							if (products.get(i).getName().contains(string))
								System.out.println(products.get(i).toString());
						}
					}
					else {
						System.out.println("Invalid Command");
					}	
					break;

				case 'a':
					if (commands.length>1){
						String productName = commands[1].trim();
						boolean found = false; // indicates whether product is in the wishlist
						for (int i=0; i<products.size();i++){
							if (products.get(i).getName().equals(productName)){
								currentUser.addToWishList(products.get(i));
								found = true; // product is in the wishlist
								System.out.println("Added to wishlist");
								break;
							}
						}
						if (!found)
							System.out.println("Product not found");	
					}
					else {
						System.out.println("Invalid Command");
					}	
					break;

				case 'r':
					if (commands.length>1){
						String productName = commands[1].trim();
						if (currentUser.removeFromWishList(productName)==null)
							System.out.println("Product not found");
						else
							System.out.println("Removed from wishlist");
					}
					else {
						System.out.println("Invalid Command");
					}
					break;

				case 'b':
						boolean bought;
						for (int i=0; i<inStock.size();i++){
							bought=false; // indicates whether the item was bought
							try{
							bought = currentUser.buy(inStock.get(i).getName());
							}catch (InsufficientCreditException E){
								System.out.println(E.getMessage());
							}
							if (bought == true)
								System.out.println("Bought "+inStock.get(i).getName());
						}
					break;

				case 'c':
					System.out.println("$"+currentUser.getCredit());
					break;

				case 'l':
					done = true;
					System.out.println("Logged Out");
					break;

				default:  //a command with no argument
					System.out.println("Invalid Command");
					break;
				}
			}
		}
	}

}
