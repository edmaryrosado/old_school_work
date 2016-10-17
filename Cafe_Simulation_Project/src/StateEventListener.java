/**
 * StateEventListener Interface
 * 
 * @author Edmary Rosado
 * @version 1.0
 *
 */
public interface StateEventListener {
	void onWaitingEvent(int customerID);
	void onStandingEvent(int customerID);
	void onBeingServedEvent(int customerID, int serverID);
	void onReturningToQueueEvent(int customerID);
	void onPayingEvent(int customerID, String checkoutTime);
	void onLeavingEvent(int customerID);
}
