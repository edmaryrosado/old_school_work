/***
 * Queue Interface
 * @author Edmary Rosado
 * @date 5/20/2016
 * @version 1.0
 */
public interface QueueInterface {
		// Transformers/Mutators
		public void enqueue(Object x);

		public Object dequeue();

		public void makeEmpty();

		// Observers/Accessors
		public Object getFront();

		public int size();

		public boolean isEmpty();

		public boolean isFull();
}
