
public class SimpleQueue<E> implements QueueADT<E> {
	private static final int INITSIZE =10;
	private int numItems;
	private E[] items;
	@SuppressWarnings("unused")
	private int frontIndex;
	@SuppressWarnings("unused")
	private int rearIndex; 
	
	@SuppressWarnings("unchecked")
	public SimpleQueue(){
		numItems = 0;
		items = (E[])(new Object[INITSIZE]);
		frontIndex = 0;
		rearIndex= numItems-1;
	}
	public boolean isEmpty(){
		if (numItems == 0) return true;
		return false;
	}
	
	public E dequeue() throws EmptyQueueException{
		if (numItems==0) throw new EmptyQueueException();
		E returnVal = items[0];
		for (int i=0; i<numItems-1;i++) 
			items[i]= items[i+1];
		numItems =numItems-1;
		return returnVal;
	}
	
	public void enqueue(E item){
		if (item==null) throw new IllegalArgumentException();
		if (items.length == numItems) expandArray();
		items[numItems] = item;
		numItems = numItems+1;
	}
	
	public E peek() throws EmptyQueueException{
		if (numItems == 0) throw new EmptyQueueException();
		return items[0];
	}
	
	public int size(){
		return numItems;
	}
	
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
