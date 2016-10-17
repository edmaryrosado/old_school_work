import java.lang.Thread;

/**
 * Barista Class
 * Handles customer threads on an individual basis and updates
 * simulation components (customer queue) in synchronized manner.
 * @author Edmary Rosado
 * @version 1.0
 */
public class Barista extends Thread {
	private int customersServed_;
	private int coffeesMade_;
	private int id_;
	private JavaBrothers cafe_;

	/**
	 * 
	 * @param cafe
	 * @param id
	 */
	public Barista(JavaBrothers cafe, int id) {
		customersServed_ = 0;
		coffeesMade_ = 0;
		cafe_ = cafe;
		id_ = id;
	}

	/**
	 * 
	 */
	public void updateCustomerCount() {
		customersServed_++;
	}

	/**
	 * 
	 * @param numberOfNewCoffeesMade
	 */
	private void updateCoffeeCount(int numberOfNewCoffeesMade) {
		coffeesMade_ += numberOfNewCoffeesMade;
	}

	public int getTotalCoffeesMade() {
		return coffeesMade_;
	}

	public int getTotalCustomersServed() {
		return customersServed_;
	}

	/**
	 * Updates the Barista's individual count of coffees
	 * made, prints to the user how many coffees were made in the
	 * single instance and forces thread to sleep during the
	 * "coffee making process".
	 * @param numberOfCoffees
	 */
	public void makeCoffees(int numberOfCoffees) {
		// Update user on Coffees being made
		System.out.println("\tBarista " + (this.id_+1)
				+ " has made "+numberOfCoffees+" coffees.");

		// Update Coffee Count
		updateCoffeeCount(numberOfCoffees);

		// Making awesome Coffees takes time...
		try {
			Thread.sleep(Coffee.PREP_TIME*numberOfCoffees);
		} catch (InterruptedException exeptionIgnored) {}
	}

	/**
	 * 
	 * @param customerID
	 * 
	 */
	public boolean cashout(Customer customer) {
		boolean cashedOut = false;
		try {
			cafe_.cashRegister_.acquire();
			customer.setCurrentState(CustomerState.PAYING);
			Thread.sleep(Coffee.OTHER_TIME);
			cafe_.cashRegister_.release();
			customer.setCashedOutStatus(true);
			cashedOut = true;
		} catch (InterruptedException e) {}
		
		return cashedOut;
	}

	/**
	 * Barista objects try to access sink semaphore. Once
	 * the Baristas "washup", they may proceed to assisting
	 * the next customer.
	 * @return
	 */
	public boolean washHands() {
		boolean washedup = false;
		try {
			cafe_.sink_.acquire();
			Thread.sleep(Coffee.OTHER_TIME);
			cafe_.sink_.release();
			System.out.println("\tBarista "+(this.id_+1)+" is washing their hands.");
			washedup = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return washedup;
	}

	public void run() {
		// While there are customers in the waiting area, continue running the Barista threads
		while (cafe_.hasCustomers()) {
			Customer customer = null;
			
			try {
				customer = cafe_.attendNextCustomer();
			} catch (InterruptedException exceptionIgnored) {}

			if (customer != null) {
				customer.setBeingHelped(true);
				customer.run();
				customer.setCurrentState(CustomerState.WAITING);
				
				int currentCoffeesToMake = 0;
				
				if (customer.getNumberOfCoffees() > JavaBrothers.MAX_ALLOWABLE_COFFEES) {
					currentCoffeesToMake = JavaBrothers.MAX_ALLOWABLE_COFFEES;
				} 
				else
					currentCoffeesToMake = customer.getNumberOfCoffees();
				
				customer.updateCoffeesLeftToMake(currentCoffeesToMake); // decrement the amount left to make if more than 3
				customer.setCurrentState(CustomerState.SERVED);
				customer.setCashedOutStatus(false); // Coffees have been made, they need to pay!
				customer.setCurrentBarista(this.id_); // Customers should always know who is serving them

				makeCoffees(currentCoffeesToMake);
				
				if(Coffee.DEBUG)
					System.out.println("Customer "+(customer.getID()+1)+" has "+customer.getNumberOfCoffees()+" coffees left to be made.");
				
				if(customer.getNumberOfCoffees()>0){
					cafe_.addCustomerToLine(customer);
					
					System.out.println("\tBarista "+(this.id_+1)+" is sending customer "+(customer.getID()+1) 
							+" back to the line with "+customer.getNumberOfCoffees()+" coffees left to be made.");
					
					customer.setCurrentState(CustomerState.REQUEUED);
					customer.setBeingHelped(false);
				} else {

					while (customer.requiresCashout()) {
						boolean successful = cashout(customer);
						
						if(!customer.requiresCashout() && successful){
							customer.setCurrentState(CustomerState.LEAVING);
						}else{
							System.out.println("\tBarista " + (this.id_+1)
									+ " and customer "
									+ customer.getArrivalTime()
									+ " are waiting for the register.");
							
							customer.setCurrentState(CustomerState.STANDING);
						}
					}
					customer.setBeingHelped(false);
					customer.setCafeStatus(false);
				}
			}
			
			if(!customer.getCafeStatus()){
				JavaBrothers.addTimeMetric(customer.getTotalTime());
				customer = null;
			}
			boolean washedup = false;
			
			// Barista can't help next customer until they wash their hands!!!
			while(!washedup){
				washedup = washHands();
			}
			
			cafe_.updatecafe();
		}
	}
}
