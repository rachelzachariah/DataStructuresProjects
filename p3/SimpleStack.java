
public class SimpleStack<E> implements StackADT<E> {
	private static final int INITSIZE =10;
	private int numItems;
	private E[] items;
	
	@SuppressWarnings("unchecked")
	public SimpleStack(){
		items = (E[])(new Object[INITSIZE]);
		numItems = 0;
	}
	 /**
	 * Checks if the stack is empty.
	 * @return true if stack is empty; otherwise false
	 */	
	public boolean isEmpty(){
		if (numItems == 0) return true;
		return false;
	}
	
	/**
     * Returns (but does not remove) the top item of the stack.
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	
	public E peek() throws EmptyStackException{
		if (numItems == 0) throw new EmptyStackException();
		else {
			return items[numItems-1];
		} 
	}
	
	/**
     * Pops the top item off the stack and returns it. 
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	public E pop() throws EmptyStackException{
		if (numItems == 0) throw new EmptyStackException();
		else {
			numItems = numItems-1;
			return items[numItems];
		} 
	}
	
	/**
     * Pushes the given item onto the top of the stack.
     * @param item the item to push onto the stack
     * @throws IllegalArgumentException if item is null.
     */
	public void push(E item){
		if (items.length == numItems) {
			this.expandArray();
		}
		items[numItems] = item;
		numItems++;
		
	}
	
	/**
     * Returns the size of the stack.
     * @return the size of the stack
     */
	public int size(){
		return numItems;
		
	}
	
	/**
     * Returns a string representation of the stack (for printing).
     * @return a string representation of the stack
     */
	public String toString(){
		String s="";
		for (int i=0; i< numItems ;i++){
			 s = s + items[i].toString() +"\n";
		}	
		return s;
	}
	private void expandArray(){
		@SuppressWarnings("unchecked")
		E[] items2 = (E[])(new Object[items.length*2]);
		for (int i =0; i<items.length; i++){
			items2[i]=items[i];
		}
		items=items2;
	}
	}

