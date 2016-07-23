/*******************************************************************************
 * Room Class
 * 
 * Room object class creates room objects or caves. 
 * 
 * Preconditions: Cave Text file successfully scanned
 * Postconditions: Room object(s) successfully created.
 * 
 * @author Edmary Rosado
 * @date 10.26.2013
 * @version 1.0
 * 
 ******************************************************************************/
public class Room {

	public boolean boas, enemy, pit;
	public boolean wampus;
	public int roomNumber;
	public String description;
	public int[] adjacentRooms;
	public EnemyType theEnemy;
	
	/**
	 * Room class constructor
	 * 
	 * The constructor for the class takes two parameters and uses
	 * them to create the Room object.
	 * 
	 * precondition: Text file read in by main driver program.
	 * postcondition: Room object created with specifics from text file.
	 * 
	 * @param specs
	 * @param roomDescription
	 */
	public Room(int[] specs, String roomDescription) {

		adjacentRooms = new int[3];
		
		this.roomNumber = specs[0];
		adjacentRooms[0] = specs[1]-1;
		adjacentRooms[1] = specs[2]-1;
		adjacentRooms[2] = specs[3]-1;

		this.description = roomDescription;
	
	}
	
	/*******************************************************************************
	 * setWampus method
	 * 
	 * This method sets the Wampus as an enemy present in the current room.
	 * 
	 * Preconditions: Enemy type established for room by main driver program.
	 * Postconditions: Wampus is set as enemy for the room.
	 * 
	 ******************************************************************************/
	
	public void setWampus() {
		wampus = true;
		enemy = true;
	}
	
	/*******************************************************************************
	 * checkWampus method
	 * 
	 * This method checks whether or not the wampus is within the room.
	 * 
	 * Preconditions: Enemies already set for certain rooms
	 * Postconditions: returns true for wampus being enemy in room
	 * 
	 * @return 
	 ******************************************************************************/	
	public boolean checkWampus() {
		return wampus;
	}
	
	/*******************************************************************************
	 * setBoas method
	 * 
	 * This method sets boas as an enemy present in the current room.
	 * 
	 * Preconditions: Enemy value selected for room set to boas.
	 * Postconditions: Boas are set as enemy for the room.
	 * 
	 ******************************************************************************/
	public void setBoas() {
		boas = true;
		enemy = true;
	}
	
	/**
	 * checkBoas Method
	 * 
	 * This method checks whether or not boas are within the room.
	 * 
	 * Precondition: Enemies already set for certain rooms
	 * Postcondition: Returns whether or not boas is the enemy present.
	 * @return true or false
	 */
	public boolean checkBoas() {
		return boas;
	}
	
	/**
	 * 	setPit Method
	 * 
	 * This method sets a pit as an enemy present in the current room.
	 * 
	 * Precondition: Enemy value selected for room set to pit.
	 * Postcondition: Pit is set as enemy for the room.
	 * 
	 */
	public void setPit() {
		pit = true;
		enemy = true;
	}
	
	/**
	 * checkPit Method
	 * 
	 * This method checks whether or not a pit is within the room.
	 * 
	 * Precondition: Enemies already set for certain rooms
	 * Postcondition: Returns whether or not a pit is the enemy present.
	 * @return true or false
	 */
	public boolean checkPit() {
		return pit;
	}
	
	/**
	 * checkEnemy Method
	 * 
	 * This method checks whether or not the room has any enemies.
	 * 
	 * Precondition: Room object is created and initialized
	 * Postcondition: Returns whether or not there is an enemy present.
	 */	
	public boolean checkEnemy() {
		return enemy;
	}
	
	/**
	 * 	getDescription Method
	 * 
	 * This method returns a room's description.
	 * 
	 * Precondition: Room object created and initialized.
	 * Postcondition: Returns current room object's description.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * getRoomNumber Method
	 * 
	 * This method returns the current room number.
	 * 
	 * Precondition: Room object created and initialized.
	 * Postcondition: Returns current room object's number.
	 * @return room number
	 * 
	 */
	public int getRoomNumber() {
		return roomNumber;
	}
	
	
	/**
	 * setEnemy Method
	 * 
	 * This method sets up an enemy for the current room object.
	 * 
	 * Precondition: Room object created and initialized.
	 * Postcondition: Room object acquires an enemy. 
	 * @param enemyNumber
	 */
	public void setEnemy(int enemyNumber){
		if(enemyNumber==EnemyType.BOAS.enemyNumber){
			this.theEnemy = EnemyType.BOAS;
			boas=true;
		}else if(enemyNumber==EnemyType.wampus.enemyNumber){
			this.theEnemy = EnemyType.wampus;
			wampus = true;
		}else if(enemyNumber==EnemyType.PIT.enemyNumber){
			this.theEnemy = EnemyType.PIT;
			pit = true;
		}
	}
	
	/**
	 * hasRoom Method
	 * 
	 * This method checks if a room object has a particular room
	 * as an adjacent room.
	 * 
	 * Precondition: Room object created and initialized.
	 * Postcondition: returns whether or not current room has the
	 * room in question as an adjacent. 
	 * @param rm
	 * @return true or false
	 */
	public boolean hasRoom(int rm){
		if(adjacentRooms[0]==rm){
			return true;
		}else if(adjacentRooms[1]==rm){
			return true;
		}else if(adjacentRooms[2]==rm){
			return true;
		}else
			return false;
	}

}
