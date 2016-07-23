
/**
 * Enemy Enum
 * 
 * The Enemy enum contains certain aspects of the dangers the game player may
 * encounter. An enemy could be boas, a pit or the Wampus.
 * 
 * Preconditions: Room object created and initialized. Postconditions: Room
 * object successfully has danger(s) appended to it.
 * 
 * @author Edmary Rosado
 * 
 */
public enum EnemyType {
	BOAS(1, "Boas"), PIT(2, "Pit"), wampus(3, "wampus");

	public String name;
	public int enemyNumber;

	/**
	 * Enemy enum constructor
	 * 
	 * @param enemyNum
	 * @param enemyName
	 */
	EnemyType(int enemyNum, String enemyName) {
		this.name = enemyName;
		this.enemyNumber = enemyNum;
	}

	public static EnemyType[] Index = new EnemyType[] { null, BOAS, PIT, wampus };

	/**
	 * getEnemyName method from enum Enemy
	 * 
	 * This method returns the name of the enemy associated with the value used
	 * as the parameter.
	 * 
	 * Precondition: Room object created and initialized and enemy assigned.
	 * Postcondition: Returns enemy type name.
	 * 
	 * @param val
	 * @return Enemy Name
	 */
	public String getEnemyName(int val) {
		return EnemyType.Index[val].toString();
	}

}