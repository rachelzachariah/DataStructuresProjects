///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  AmazonStore.java
// File:             DLinkedList.java
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
//
//////////////////////////// 80 columns wide //////////////////////////////////

public class DLinkedList<E> implements ListADT<E>{
	private Listnode<E> head;
	private int numItems;
	
	public DLinkedList() {
		head = null;
		numItems = 0;
	}

	public void add(E item) { 
		if (item == null)
			throw new IllegalArgumentException();
		Listnode <E> newnode = new Listnode<E>(item);
		if (head == null) //list is empty
			head = newnode;
		else{
			Listnode<E> curr = head;
			while (curr.getNext() !=null) //traverse to end of list
				curr = curr.getNext();
			curr.setNext(newnode);
			newnode.setPrev(curr);
		}
		numItems++;
	}

	public void add(int pos, E item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (pos > numItems || pos < 0)
			throw new IndexOutOfBoundsException();
		Listnode<E> newnode = new Listnode<E>(item);
		Listnode<E> curr = head;
		if (head == null){ //list is empty
			head = newnode;
		}
		else{
			if (pos > 0) {
				for (int i = 0; i<pos-1; i++)
					curr = curr.getNext();
				if (pos == numItems){
					curr.setNext(newnode);
					newnode.setPrev(curr);
					}
				else{
					newnode.setNext(curr.getNext());
					newnode.setPrev(curr);
					curr.getNext().setPrev(newnode);
					curr.setNext(newnode);
				}
			}
			else { //pos = 0
				newnode.setNext(curr);
				newnode.getNext().setPrev(newnode);
				head = newnode;
			}
		}
		numItems++;
	}

	public boolean contains(E item) {
		Listnode<E> curr = head;
		for (int i=0; i < numItems; i++) {
			if (curr.getData() == item)
				return true;
			curr = curr.getNext();			
		}		
		return false;
	}

	public E get(int pos) {
		if (pos >= numItems || pos < 0)
			throw new IndexOutOfBoundsException();
		else{
			Listnode<E> curr = head;
			if (pos > 0) { //traverse to pos
				for (int i = 0; i<pos; i++)
					curr = curr.getNext();
			}
			return curr.getData();
		}
	}

	public boolean isEmpty() {
		if (numItems == 0)
			return true;
		return false;
	}

	public E remove(int pos) {
		if (pos >= numItems || pos < 0)
			throw new IndexOutOfBoundsException();
		else{
			Listnode<E> curr = head;
			if (pos > 0) {
				for (int i = 0; i<pos; i++)
					curr = curr.getNext();
				if (pos == numItems-1) //special case : end of the list
					curr.getPrev().setNext(null);
				else{
					curr.getPrev().setNext(curr.getNext());
					curr.getNext().setPrev(curr.getPrev());
				}
			}
			else // pos = 0
				head = curr.getNext();
			numItems= numItems-1;
			return curr.getData();
		}
	}

	public int size() {
		return numItems;
	}
}
