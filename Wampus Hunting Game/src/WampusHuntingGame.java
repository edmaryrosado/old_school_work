import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/*******************************************************************************
 * Game Class
 *
 * Driver program for the Wampus hunting game.
 *
 * Preconditions: Postconditions:
 *
 * @author Edmary Rosado
 * @version 1.0
 *
 ******************************************************************************/

public class WampusHuntingGame {
	public Room[] rooms;
	public int arrows, enemies;
	private int numberOfRooms;
	private int currentRoom;
	private final int startingRoom = 0;
	public static Scanner s;
	public String[][] GameBoard;
	private boolean playerAlive = true;

	/**
	 * Main method
	 *
	 * This method starts the game program and feeds in the file to the program.
	 *
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws IOException {
		try {
			FileReader r = new FileReader("cave.ini");
			s = new Scanner(r);
			new WampusHuntingGame();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Game class constructor
	 *
	 * The class constructor sets up the initial reading of the text file and
	 * calls setGameBoard and playGame methods to create the board and get the
	 * game in motion.
	 *
	 * precondition: Program started postcondition: Game board created and game
	 * started by calling playGame method.
	 *
	 */
	public WampusHuntingGame() {
		arrows = 3;
		enemies = 4;
		currentRoom = startingRoom; // place character in room 1
		setGameBoard();
		s = new Scanner(System.in);
		System.out.println("***************************************");
		System.out.println("Welcome to **Wampus Hunting!**\n");
		System.out.println("***************************************");
		playGame();
	}

	/**
	 * setGameBoard Method
	 *
	 * This method creates the game board by scanning through each line of the
	 * game text file.
	 *
	 */
	public void setGameBoard() {
		numberOfRooms = s.nextInt();
		GameBoard = new String[numberOfRooms][2];
		while (s.hasNext()) {
			if (s.nextLine() != null) {
				for (int i = 0; i < numberOfRooms; i++) {
					for (int j = 0; j < 2; j++) {
						GameBoard[i][j] = s.nextLine();
					}
				}
			}
		}
		rooms = new Room[GameBoard.length];
		for (int i = 0; i < GameBoard.length; i++) {

			String[] n = GameBoard[i][0].split(" ");
			int[] rmSpecs = new int[n.length];

			for (int j = 0; j < n.length; j++) {
				rmSpecs[j] = Integer.parseInt(n[j]);
			}

			rooms[i] = new Room(rmSpecs, GameBoard[i][1].toString());
		}

		setTraps();

	}

	/**
	 * setTraps Method
	 *
	 * This method sets up the traps for the game and recursively calls itself
	 * until all enemies are assigned.
	 *
	 */
	public void setTraps() {
		Random rand = new Random();
		int val = rand.nextInt(numberOfRooms - 1) + 1;
		if (rooms[val].enemy) {
			setTraps();
		} else {
			if (enemies != 0) {
				if (enemies == 4) {
					rooms[val].setWampus();
				}
				if (enemies == 3) {
					rooms[val].setBoas();
				}
				if (enemies == 2) {
					rooms[val].setPit();
				}
				if (enemies == 1) {
					rooms[val].setBoas();
				}
				enemies--;
				setTraps();
			}
		}

	}

	/**
	 * playGame Method
	 *
	 * This method sets the game in motion. It calls printPosition, printStatus
	 * and retrieveAction methods to engage with the user.
	 *
	 * Precondition: Game board set and specifics already in place
	 * Postcondition: Game is set in motion, status and position presented to
	 * user. While user is alive, the game goes on a loop. If the user dies, the
	 * method prints out game over.
	 *
	 */
	public void playGame() {

		while (playerAlive) {
			printPosition();
			printStatus();
			retrieveAction();
		}
		System.out.println("Game Over.");
	}

	/**
	 * retrieveAction Method
	 *
	 * This method prompts the user for their choice of action, whether it be
	 * shooting or moving and feeds it to the performAction method which carries
	 * out the commands.
	 *
	 * Precondition: Game set in motion. Postcondition: Action from user used to
	 * participate in game.
	 *
	 */
	public void retrieveAction() {
		String actionChoice;
		int roomChoice;

		System.out.println("(M)ove or (S)hoot?");
		actionChoice = s.next();
		System.out.println("Which room?");
		roomChoice = s.nextInt();

		performAction(actionChoice, roomChoice);
	}

	/**
	 * performAction Method
	 *
	 * This method carries out the actions of the user.
	 *
	 * Precondition: User selects an action and a room to move or shoot to.
	 * Postcondition: Action is carried out and player's live or action is
	 * evaluated.
	 *
	 * @param action
	 * @param room
	 */
	public void performAction(String action, int room) {

		if (action.compareToIgnoreCase("M") == 0) {
			// check if chosen value is adjacent room
			if (rooms[currentRoom].hasRoom(room - 1)) {

				currentRoom = room - 1;

				if (rooms[currentRoom].checkPit()) {
					System.out
							.println("Oh no! You fell into a pit, slow and painful death");
					playerAlive = false;
				} else if (rooms[currentRoom].checkEnemy()) {
					System.out.println("Room full of enemies!");
					playerAlive = false;
				} else if (rooms[currentRoom].checkBoas()) {
					System.out
							.println("The boas are out for blood! Instant death!");
					playerAlive = false;
				} else if (rooms[currentRoom].checkWampus()) {
					System.out.println("DEATH BY WAMPUS! Game Over!");
					playerAlive = false;
				}
				System.out.println();
			}
			// if not, print insult
			else {
				System.out
						.println("Dimwit! You can't get to there from here!!!\n");
				System.out.println("There are tunnels to rooms "
						+ rooms[currentRoom].adjacentRooms[0] + ", "
						+ rooms[currentRoom].adjacentRooms[1] + ", and "
						+ rooms[currentRoom].adjacentRooms[2]);
				retrieveAction();

			}
		} else if (action.compareToIgnoreCase("S") == 0) {
			if (arrows != 0) {
				if (rooms[currentRoom].hasRoom(room - 1)) {
					arrows = arrows - 1;
					if (rooms[room - 1].checkWampus()) {

						System.out
								.println("\nYour arrow goes down the tunnel to room "+room+" and finds its mark!");
						System.out
								.println("You shot the Wampus!!!\t**You Win!**");
						System.out.println("Victory is yours!");
						System.exit(0);
					} else {
						System.out
								.println("Your arrow goes down the tunnel and is lost. You missed.");
					}
				} else {
					System.out.println("You blind? You can't shoot at a wall!");
					System.out.println("There are tunnels to rooms "
							+ rooms[currentRoom].adjacentRooms[0] + ", "
							+ rooms[currentRoom].adjacentRooms[1] + ", and "
							+ rooms[currentRoom].adjacentRooms[2]);
					retrieveAction();

				}
			} else {
				System.out
						.println("You were out of arrows and Wampus came after you. Game Over.");
			}
		}
	}

	/**
	 * checkAdjacentRooms Method
	 *
	 * This method returns to the user certain sensory details about the
	 * adjacent rooms.
	 *
	 * Precondition: Game started and user options set in motion. Postcondition:
	 * Adjacent room sensory details printed out for user's knowledge.
	 */
	public void checkAdjacentRooms() {

		if (rooms[rooms[currentRoom].adjacentRooms[0]].checkBoas()
				|| rooms[rooms[currentRoom].adjacentRooms[1]].checkBoas()
				|| rooms[rooms[currentRoom].adjacentRooms[2]].checkBoas()) {
			System.out.println("You hear a faint hissing noise.");
		}
		if (rooms[rooms[currentRoom].adjacentRooms[0]].checkWampus()
				|| rooms[rooms[currentRoom].adjacentRooms[1]].checkWampus()
				|| rooms[rooms[currentRoom].adjacentRooms[2]].checkWampus()) {
			System.out.println("You smell some nasty Wampus!");
		}
		if (rooms[rooms[currentRoom].adjacentRooms[0]].checkPit()
				|| rooms[rooms[currentRoom].adjacentRooms[1]].checkPit()
				|| rooms[rooms[currentRoom].adjacentRooms[2]].checkPit()) {
			System.out.println("You smell a dank odor.");
		}
	}

	/**
	 * printPosition Method
	 *
	 * This method prints to the user the current room that they are in and the
	 * number of arrows they have.
	 *
	 * Precondition: Game started and user options set in motion. Postcondition:
	 * Position details printed out for user's knowledge.
	 */
	public void printPosition() {
		System.out.println("You are in room " + (currentRoom + 1) + ".");
		System.out.println("You have " + arrows + " arrows left.\n");
	}

	/**
	 * printStatus Method
	 *
	 * This method prints the user's current location and the adjacent rooms, as
	 * well as calls the checkAdjacentRooms method which returns whether or not
	 * there is an enemy sensory description (smell or sound).
	 *
	 * Precondition: Game started and user options set in motion. Postcondition:
	 * Room description and adjacent room details printed out for user's
	 * knowledge.
	 */
	public void printStatus() {

		System.out.println(rooms[currentRoom].description);
		System.out.println("There are tunnels to rooms "
				+ (rooms[currentRoom].adjacentRooms[0] + 1) + ", "
				+ (rooms[currentRoom].adjacentRooms[1] + 1) + ", and "
				+ (rooms[currentRoom].adjacentRooms[2] + 1));
		checkAdjacentRooms();
	}
}
