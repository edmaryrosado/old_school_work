/***
 * Queue for Restaurant
 * @author Edmary Rosado
 * @version 1.0
 */
public class Queue {
	private class node {
		public Object data;
		public node next;
	}

	node front, back;
	int count;

	public Queue() {
		makeEmpty();
	}

	/** Transformers/Mutators
	 * 
	 * @param x
	 */
	public void enqueue(Object x) {
		node nn = new node();
		nn.data = x;
		nn.next = null;
		if (isEmpty())
			front = nn;
		else
			back.next = nn;
		back = nn;
		count++;
	}

	/**
	 * 
	 * @return
	 */
	public Object dequeue() {
		if (isEmpty())
			return null;
		Object frontitem = getFront();
		front = front.next;
		count--;
		if (count == 0)
			back = null;
		return frontitem;
	}

	/**
	 * 
	 */
	public void makeEmpty() {
		front = back = null;
		count = 0;
	}

	/** Observers/Accessors
	 * 
	 * @return
	 */
	public Object getFront() {
		if (isEmpty())
			return null;
		return front.data;
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return count;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return count == 0;
	}
}
