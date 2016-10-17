import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/***
 * Customer State Enum
 * Implements StateEventListener interface for event handling purposes.
 * @author Edmary Rosado
 * @version 1.0
 */
public enum CustomerState implements StateEventListener{
	WAITING(0), STANDING(1), SERVED(2), REQUEUED(3), PAYING(4), LEAVING(5);

	private int code;

	private CustomerState(int code) {
		this.code = code;
	}

	int getCode() {
		return code;
	}

	private static final Map<Integer, CustomerState> lookup = new HashMap<Integer, CustomerState>();

	static {
		for (CustomerState reg : EnumSet.allOf(CustomerState.class))
			lookup.put(reg.getCode(), reg);
	}
	
	static CustomerState get(int code) {
		return lookup.get(code);
	}
	
	@Override
	public void onWaitingEvent(int customerID) {
		System.out.println("Customer "+(customerID+1)+" is waiting to be helped.");
	}

	@Override
	public void onStandingEvent(int customerID) {
		System.out.println("Customer "+(customerID+1)+" is standing.");
	}

	@Override
	public void onBeingServedEvent(int customerID, int serverID) {
		System.out.println("Customer "+(customerID+1)+" is being served by server "+(serverID+1)+".");
	}
	
	@Override
	public void onReturningToQueueEvent(int customerID) {
		System.out.println("Customer "+ (customerID+1) + " is returning to the waiting area.");
	}

	@Override
	public void onPayingEvent(int customerID, String checkoutTime) {
		System.out.println("Customer "+(customerID+1)+" is paying at "+checkoutTime+".");
	}
	
	@Override
	public void onLeavingEvent(int customerID){
		System.out.println("Customer "+(customerID+1)+" left the cafe.");
	}
}
