import java.util.Date;

/**
 * Customer class
 * Extends thread and implements StateEventListener.
 * Contains all information of a customer and their respective states.
 * @author Edmary Rosado
 * @version 1.0
 */
public class Customer extends Thread implements StateEventListener{
	private String arrivalTime_;
	private String checkoutTime_;
	private int numberOfCoffees_;
	private int id_, currentBaristaID_;
	private boolean beingHelped_, stillInCafe_, cashedOut_;
	private CustomerState state_;
	private long startTime_, elapsedTime_;

	public Customer(){}

	/**
	 *
	 * @param arrivalTime
	 * @param numberOfCoffees
	 */
	public Customer(int id, String arrivalTime, int numberOfCoffees){
		arrivalTime_ = arrivalTime;
		numberOfCoffees_ = numberOfCoffees;
		elapsedTime_ = 0L;
		this.id_ = id;
	}
	
	public int getID(){
		return id_;
	}

	/**
	 *
	 * @return
	 */
	public String getArrivalTime(){
		return arrivalTime_;
	}

	/**
	 *
	 * @return
	 */
	public int getNumberOfCoffees(){
		return numberOfCoffees_;
	}

	/**
	 *
	 * @return
	 */
	public String getCheckoutTime(){
		return checkoutTime_;
	}

	/**
	 * 
	 * @param decrementValue
	 */
	public void updateCoffeesLeftToMake(int decrementValue){
		numberOfCoffees_-=decrementValue;
	}

	/**
	 *
	 * @param numberOfCoffees
	 */
	public void setNumberOfBurritos(int numberOfCoffees){
		numberOfCoffees_ = numberOfCoffees;
	}

	/**
	 */
	public void setCheckoutTime(){
		checkoutTime_ = new java.text.SimpleDateFormat("hh:mm:ss a").format(new Date());
	}
	
	/**
	 * Sets the start time of customer
	 */
	public void startWaitTime(){
		startTime_ = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCurrentState(){
		return state_.getCode();
	}
	
	/**
	 * 
	 * @param state
	 */
	public void setCurrentState(CustomerState state){
		if(state_ != state){
			state_ = state;
			onNewState();
		}
	}
	
	/**
	 * 
	 * 
	 * @param val
	 */
	public void setBeingHelped(boolean val){
		beingHelped_ = val;
	}
	
	/**
	 * 
	 * @param val
	 */
	public void setCafeStatus(boolean val){
		stillInCafe_ = val;
	}
	
	public boolean getCafeStatus(){
		return stillInCafe_;
	}
	
	public void setCashedOutStatus(boolean val){
		setCheckoutTime();
		cashedOut_ = val;
	}
	
	public boolean requiresCashout(){
		if(!cashedOut_)
			return true;
		else
			return false;
	}
	
	public void setCurrentBarista(int baristaID){
		currentBaristaID_ = baristaID;
	}
	
	public int getCurrentBarista(){
		return currentBaristaID_;
	}
	
	public long getTotalTime() {
		elapsedTime_ = (new Date()).getTime() - startTime_;
		return elapsedTime_;
	}
	
	/**
	 * 
	 * @param serverID
	 */
	public void onNewState(){
		
		switch(CustomerState.get(getCurrentState())){
			case WAITING:
				if(Coffee.DEBUG){System.out.println("WAITING");}
				state_.onWaitingEvent(this.id_);
				break;
			case STANDING:
				if(Coffee.DEBUG){System.out.println("STANDING");}
				state_.onStandingEvent(this.id_);
				break;
			case SERVED:
				if(Coffee.DEBUG){System.out.println("SERVED");}
				state_.onBeingServedEvent(this.id_, this.getCurrentBarista());
				break;
			case PAYING:
				if(Coffee.DEBUG){System.out.println("PAYING");}
				state_.onPayingEvent(this.id_, checkoutTime_);
				break;
			case REQUEUED:
				if(Coffee.DEBUG){System.out.println("REQUEUED");}
				state_.onReturningToQueueEvent(this.id_);
				break;
			case LEAVING:
				if(Coffee.DEBUG){System.out.println("LEAVING");}
				state_.onLeavingEvent(this.id_);
				beingHelped_ = false;
			default:
				break;
		}
	}
	
	public void run(){
		
		while(beingHelped_ && stillInCafe_){
			System.out.println(".");
			//TODO: Make a timer for total time in queue
			if(Coffee.DEBUG)
				System.out.println("Customer "+(this.id_+1)+" thread lives.");
		}
		
		if(Coffee.DEBUG)
			System.out.println("Customer "+(this.id_+1)+" thread stopped.");
	}
	
	/**
	 * Implemented event methods for customer objects
	 */

	@Override
	public void onWaitingEvent(int customerID) {
		state_.onWaitingEvent(customerID);
	}

	@Override
	public void onStandingEvent(int customerID) {
		state_.onStandingEvent(customerID);
	}

	@Override
	public void onBeingServedEvent(int customerID, int serverID) {
		state_.onBeingServedEvent(customerID, serverID);
	}

	@Override
	public void onPayingEvent(int customerID, String checkoutTime) {
		state_.onPayingEvent(customerID, checkoutTime);
	}

	@Override
	public void onLeavingEvent(int customerID) {
		state_.onLeavingEvent(customerID);
		setBeingHelped(false);
	}

	@Override
	public void onReturningToQueueEvent(int customerID) {
		state_.onReturningToQueueEvent(customerID);
	}
}
