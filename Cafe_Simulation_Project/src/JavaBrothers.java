import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.Collections;

/**
 * Java Brothers class
 * Initializes main simulation components and starts barista threads.
 * @author Edmary Rosado
 * @version 1.0
 */
public class JavaBrothers {

	// Customer Queues
	private boolean cafeAtCapacity_;
	private Queue buildingEntranceQueue_; // Patrons from Entire Building
	private ArrayList<Customer> waitingArea_; // Patrons in Waiting Area

	// Cafe Constants
	public static final int MAX_ALLOWABLE_COFFEES = 4;
	private final int NUMBER_OF_BARISTAS = 3;
	private final int MAX_CUSTOMER_CAPACITY = 15;
	private final int MAX_SINKS = 1;
	private final int MAX_REGISTERS = 1;

	// Shared Resources
	Semaphore cashRegister_;
	Semaphore sink_;

	// The Java Brothers baristas
	Barista[] baristas_;
	
	// Time Metrics
	private static long totalTimeAcrossCustomers_;
	private int numberOfCustomersInScenario_;
	private static int totalNumberOfCoffees_;

	/**
	 * Java Brothers Class Constructor
	 * @param customers
	 */
	public JavaBrothers(Queue customers) {
		customers.size();
		buildingEntranceQueue_ = new Queue();
		waitingArea_ = new ArrayList<Customer>();
		numberOfCustomersInScenario_ = customers.size();
		init(customers);
	}

	/**
	 * Initializes customer queues utilized in the cafe simulation
	 * and initializes the sink and register semaphore objects.
	 * @param customers
	 */
	public void init(Queue customers) {
		// Initialize Baristas
		baristas_ = new Barista[NUMBER_OF_BARISTAS];
		for (int s = 0; s < NUMBER_OF_BARISTAS; s++) {
			baristas_[s] = new Barista(this, s);
		}

		// Initialize Sink and Cash Register Semaphores
		// Each only accessable by 1 thread at a time
		sink_ = new Semaphore(MAX_SINKS);
		cashRegister_ = new Semaphore(MAX_REGISTERS);

		// Initialize Customer Queue
		// The building entrance queue will be the structure of which
		// each customer in the scenario will be handled.
		if (customers.size() != 0) {
			buildingEntranceQueue_ = customers;
		}
	}

	/**
	 * Organizes the waiting area by shortest order next using the number of
	 * burritos left to make.
	 */
	public void shortestOrderNext() {
		int i,j;
		for (i = 1; i < waitingArea_.size(); i++) {
			Customer customer = new Customer();
			customer = waitingArea_.get(i);

			j = i;
			while ((j > 0)
					&& (waitingArea_.get(j - 1).getNumberOfCoffees() > customer
							.getNumberOfCoffees())) {
				waitingArea_.set(j, waitingArea_.get(j - 1));
				j--;
			}
			waitingArea_.set(j, customer);
		}
	}

	/**
	 * Adds new customer into ArrayList and then calls shortestOrderNext()
	 * for re-ordering.
	 */
	public synchronized void addCustomerToLine(Customer customer) {
		waitingArea_.add(customer);
		shortestOrderNext();
	}

	/**
	 * Returns whether the waiting area has customers
	 * in it or not.
	 * @return
	 */
	public boolean hasCustomers() {
		if (isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns whether the waiting area of the cafe
	 * is at capacity.
	 * @return
	 */
	public boolean isFull() {
		return cafeAtCapacity_;
	}

	/**
	 * Returns true if waiting area has 
	 * zero customers, false otherwise.
	 * @return
	 */
	public boolean isEmpty() {
		return waitingArea_.size() == 0;
	}

	/**
	 * Updates the waiting queue with the entire collection of
	 * customers waiting in and out of the building. If there are 
	 * people waiting to be served outside but the inside of the
	 * building is not at capacity, customers can enter until 
	 * the building (waiting area) is at capacity and sorted
	 * by shortest order next.
	 */
	public void updatecafe() {
		if (waitingArea_.size() == MAX_CUSTOMER_CAPACITY) {
			cafeAtCapacity_ = true;
		} else {
			if (buildingEntranceQueue_.size() != 0) {
				while ((waitingArea_.size() != MAX_CUSTOMER_CAPACITY)
						&& (buildingEntranceQueue_.size() != 0)) {
					Customer customer = (Customer) buildingEntranceQueue_
							.dequeue();
					customer.startWaitTime(); // once in the building, start the wait time
					waitingArea_.add(customer);
				}
				
				shortestOrderNext();
			}
		}
	}

	/**
	 * Returns the next customer in the queue to be
	 * attended by the next available barista.
	 * @return First Customer Object in ArrayList
	 * @throws InterruptedException
	 */
	public synchronized Customer attendNextCustomer()
			throws InterruptedException {
		Customer customer = null; // Get First in Line
		int front = 0; // index of first in ArrayList

		if (waitingArea_.size() != 0) {
			customer = waitingArea_.get(front);
			System.out.println("Customer " + (customer.getID() + 1)
					+ " is approaching the counters.");
			waitingArea_.remove(front);
			Collections.rotate(waitingArea_, 1);
		}

		return customer;
	}
	
	/**
	 * Increments the total time across customers in
	 * milliseconds.
	 * @param totalTime
	 */
	public static void addTimeMetric(long totalTime) {
		totalTimeAcrossCustomers_ += totalTime;
		
	}
	
	/**
	 * Increments the total number of burritos across
	 * customers.
	 * @param totalBurritos
	 */
	public void getTotalBurritosMade(){
		
		totalNumberOfCoffees_ = baristas_[0].getTotalCoffeesMade()
				+ baristas_[1].getTotalCoffeesMade()
				+ baristas_[2].getTotalCoffeesMade();
		
		if(Coffee.DEBUG){
			System.out.println("barista 1 made a total of: "+ baristas_[0].getTotalCoffeesMade()+" burritos.");
			System.out.println("barista 2 made a total of: "+ baristas_[1].getTotalCoffeesMade()+" burritos.");
			System.out.println("barista 3 made a total of: "+ baristas_[2].getTotalCoffeesMade()+" burritos.");
			System.out.println("Total Burritos Made: "+ totalNumberOfCoffees_ +" burritos.");
		}
	}
	
	/**
	 * Prints simulation metrics
	 */
	public void printSimulationStats(){
		System.out.println("****************************************");
		System.out.println("cafe Simulation Statistics");
		System.out.println("****************************************");
		getTotalBurritosMade();
		NumberFormat formatter = new DecimalFormat("#0.0000");
		System.out.println("Average Wait Time: "+
				formatter.format(((double)(totalTimeAcrossCustomers_/1000)/(double)numberOfCustomersInScenario_))+" seconds");
		System.out.println("Average # of Burritos: "+
				formatter.format(((double)totalNumberOfCoffees_/(double)numberOfCustomersInScenario_))+" burritos");
		System.out.println("****************************************");

	}

	public void runSimComponents() {
		System.out.println("**************************************************");
		System.out.println("Java Brothers Simulation has started...");
		System.out.println("**************************************************");
		System.out.println("Java Brothers is Open for Business!\n");

		// Open Java Brothers for business, populate initial coffee line queue!
		updatecafe();
		baristas_[0].start();
		baristas_[1].start();
		baristas_[2].start();

		try {
			baristas_[0].join();
			baristas_[1].join();
			baristas_[2].join();
		} catch (InterruptedException ignored) {}

		// Print Scenario Stats
		System.out.println();
		printSimulationStats();
		System.out.println();
		System.out.println("Java Brothers Simulation Terminated");
	}
}
