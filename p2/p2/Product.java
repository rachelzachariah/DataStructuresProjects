///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  (name of main application class)
// File:             (name of this class's file)
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
//
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * Stores the name, category, price and rating of a product
 */
public class Product {
	
	private String name;
	private String category;
	private int price;
	private float rating;
	
	/**
     * Constructs a Product with a name, category, price and rating. 
     * 
     * @param name name of product
     * @param category category of product
     * @param price price of product in $ 
     * @param rating rating of product out of 5
     */
	public Product(String name, String category, int price, float rating){
		this.name = name;
		this.category = category;
		this.price = price;
		this.rating = rating;
	}
	
	/** 
     * Returns the name of the product
     * @return the name
     */
	public String getName(){
		return name;
	}
	
	/** 
     * Returns the category of the product
     * @return the category
     */
	public String getCategory(){
		return category;
	}
	
	/** 
     * Returns the price of the product
     * @return the price
     */
	public int getPrice(){
		return price;
	}
	
	/** 
     * Returns the rating of the product
     * @return the rating
     */
	public float getRating(){
		return rating;
	}
	
	/** 
     * Returns the Product's information in the following format: <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public String toString(){
		return name + "\t" +"[Price:$" + price +" "+ "Rating:"+ rating +" "+"stars]" ;
	}

}
