import java.util.Date;
import java.util.Random;

/**
 * Coffee Class
 * Main Program Driver for Java Brothers Simulation
 * @author Edmary Rosado Vega
 * @version 1.0
 * @course CISC-640 Summer 2016 Operating Systems
 */
public class Coffee {

	public static boolean DEBUG; // DEBUG Flag
	public static int PREP_TIME; // in milliseconds
	public static int OTHER_TIME; // in milliseconds

	private JavaBrothers cafe_;

	private int randomNumberGeneratorSeed_;
	private int numberOfCustomers_;
	
	// Array of Customers with individual arrival times
	// and Randomly generated number of Coffee orders
	private Queue customerQueue_;

	/**
	 * Class Constructor
	 * @param randomNumberSeed
	 * @param numberOfCustomers
	 */
	public Coffee(int randomNumberSeed, int numberOfCustomers, boolean debugFlag, int simSpeed){
		randomNumberGeneratorSeed_ = randomNumberSeed;
		numberOfCustomers_ = numberOfCustomers;
		PREP_TIME = simSpeed; // Prep time takes the longest
		OTHER_TIME = simSpeed/2; // Cashout and hand washing speed should be fast
		DEBUG = debugFlag;
		
		initScenario();
		runJavaBrothersScenario();
	}

	/**
	 * Initializes scenario with user specified number of customers
	 * and randomly generated number of coffees per customer.
	 */
	public void initScenario(){
		customerQueue_ = new Queue();
		Random rand = new Random();
		
		for(int i=0; i<numberOfCustomers_; i++){
			int coffeeOrder = rand.nextInt(randomNumberGeneratorSeed_)+1;
			String date = new java.text.SimpleDateFormat("h:mm:ss a").format(new Date());

			Customer customer = new Customer(i, date, coffeeOrder);
			
			if(DEBUG)
				System.out.println("Customer "+(i+1)+" wants to order "+coffeeOrder+" coffees.");
			
			customerQueue_.enqueue(customer);
		}
	}

	/**
	 * Initializes restaurant with the customer queue generated
	 * and runs the Java Brothers simulation.
	 */
	public void runJavaBrothersScenario(){
		cafe_ = new JavaBrothers(customerQueue_);
		cafe_.runSimComponents();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static int parameterCheck(String str){
		int num = -1;
		try {
			num = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			System.err.println("Argument" + str + " must be an integer.");
			System.exit(1);
		}
		return num;
	}

	/**
	 * Main program driver
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// DEFAULT Values for Simulation if user does not enter parameters
		Random rand = new Random();
		final int DEFAULT_GENERATOR_SEED = 20;
		int numberOfCustomers = rand.nextInt(DEFAULT_GENERATOR_SEED)+1;
		int randomGeneratorSeed = rand.nextInt(DEFAULT_GENERATOR_SEED)+1;
		int simSpeed = 2000; // Middle Speed; Fast=1000; Slow=3000
		boolean debugFlag = false;
		
		// Check for parameters
		if(args.length>0){
			for(int i=0; i<args.length; i++){
				if(i==0){
					numberOfCustomers = parameterCheck(args[0]);
				}
				else if(i==1){
					randomGeneratorSeed = parameterCheck(args[1]);
				}else if(i==2){
					try{
						debugFlag = Boolean.parseBoolean(args[2]);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else if(i==3){
					if(args[3].equalsIgnoreCase("F")){
						simSpeed = 1000;
					} else if(args[3].equalsIgnoreCase("M")){
						// Default, do nothing
					} else if(args[3].equalsIgnoreCase("S")){
						simSpeed = 3000;
					}
				}else{
					// Output to user appropriate format for parameters
					System.out.println("ERROR: Too many parameters! Accepted format:"+
							"\njava coffee <numberOfCustomers>"+
							"\njava coffee <numberOfCustomers> <randomGeneratorSeed>"+
							"\njava coffee <numberOfCustomers> <randomGeneratorSeed> <debugFlag>"+
							"\njava coffee <numberOfCustomers> <randomGeneratorSeed> <boodebugFlaglean> <simSpeed('S' or 'M' or 'F')>");
				}
			}
		}else{
			// If no parameters, run default scenario generated
			System.out.println("\nWARNING: User did not provide input parameters, system will now run with the following generated parameters:");
			System.out.println("Number of Customers: "+numberOfCustomers);
			System.out.println("Random Number Generator Seed: "+randomGeneratorSeed);
			System.out.println("Simulator Speed is defaulted to Medium: "+simSpeed+" milliseconds for preptime and "+
					(simSpeed/2)+" milliseconds for other time (checkout, wash hands, etc)");
			System.out.println("DEBUG_FLAG: "+debugFlag);
			System.out.println();
		}
		
		// Start new Coffee session
		new Coffee(randomGeneratorSeed, numberOfCustomers, debugFlag, simSpeed);
	}
}
