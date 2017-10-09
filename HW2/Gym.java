package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Gym implements Runnable {
	private static final int GYM_SIZE = 30;
	private static final int GYM_REGISTERED_CLIENTS = 10000;
	
	// Made the number of plates variables in case the Gym buys more or loses some.
	private static final int NUM_SMALL_PLATES = 110;
	private static final int NUM_MEDIUM_PLATES = 90;
	private static final int NUM_LARGE_PLATES = 75;
	
	// Technically there should be a variable for the number of each machine type, but they're all 5 anyway
	private static final int NUM_MACHINE_TYPE = 5; 
	
	private Map<WeightPlateSize, Integer> noOfWeightPlates;
	static Map<WeightPlateSize, Integer> remainingNoOfWeightPlates; // Used to calculate remaining plates 
	private static Map<WeightPlateSize, Integer> tempNoOfWeightPlates; // pointer to noOfWeightPlates; see Gym.getNoWeightPlates()
	static Map<ApparatusType, Semaphore> apparatusTypeSemaphoreMap; // Map of apparatusType to Semaphore to prevent repeated code
	
	private Set<Integer> clients;
	private ExecutorService executor;
	private Random r;
	static Map<Integer, ApparatusType> apparatusMap = new HashMap<Integer, ApparatusType>();
	
	// There are only 5 of each gym apparatus type
	final static Semaphore LEGPRESSMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore BARBELL = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore HACKSQUATMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore LEGEXTENSIONMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore LEGCURLMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore LATPULLDOWNMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore PECDECKMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	final static Semaphore CABLECROSSOVERMACHINE = new Semaphore(NUM_MACHINE_TYPE);
	// Binary semaphore used to acquire the permissions to get plates.
	final static Semaphore lPlateAccess= new Semaphore(1);
	final static Semaphore mPlateAccess= new Semaphore(1);
	final static Semaphore sPlateAccess= new Semaphore(1);
	final static Semaphore lPlates= new Semaphore(NUM_LARGE_PLATES);
	final static Semaphore mPlates= new Semaphore(NUM_MEDIUM_PLATES);
	final static Semaphore sPlates= new Semaphore(NUM_SMALL_PLATES);
	
	public Gym() {
		// Initialize the the Maps
		noOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, NUM_LARGE_PLATES);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, NUM_MEDIUM_PLATES);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, NUM_SMALL_PLATES);
		
		remainingNoOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, NUM_LARGE_PLATES);
		remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, NUM_MEDIUM_PLATES);
		remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, NUM_SMALL_PLATES);
		
		// This is just a pointer noOfWeightPlates, since it won't be modified.
		tempNoOfWeightPlates = noOfWeightPlates;
		
		// Map of ApparatusTypes to Semaphore so there's no switch or if/elseif with repeated code
		apparatusTypeSemaphoreMap = new HashMap<ApparatusType, Semaphore>();
		apparatusTypeSemaphoreMap.put(ApparatusType.LEGPRESSMACHINE, LEGPRESSMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.BARBELL, BARBELL);
		apparatusTypeSemaphoreMap.put(ApparatusType.HACKSQUATMACHINE, HACKSQUATMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.LEGEXTENSIONMACHINE, LEGEXTENSIONMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.LEGCURLMACHINE, LEGCURLMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.LATPULLDOWNMACHINE, LATPULLDOWNMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.PECDECKMACHINE, PECDECKMACHINE);
		apparatusTypeSemaphoreMap.put(ApparatusType.CABLECROSSOVERMACHINE, CABLECROSSOVERMACHINE);
		
		r = new Random();
		clients = new HashSet<Integer>();
	}
	
	/**
	 * Returns a static variable which is just a copy of noOfWeightPlates
	 * In the assignment UML diagram, noOfWeightPlates is supposed to be private static,
	 * which means that it cannot be accessed by other classes. 
	 * However, Client.generateRandom() needs the Map to pass to Exercise.generateRandom() 
	 * to know the keySpace. 
	 * Therefore, I created a (public) static temp variable to that references noOfWeightPlates 
	 * so it can be passed.
	 * @return Map<WeightPlateSize, Integer>
	 */
	public static Map<WeightPlateSize, Integer> getNoOfWeightPlates(){
		return tempNoOfWeightPlates;
	}
	
	public void run(){
		// Thread pool
		executor = Executors.newFixedThreadPool(GYM_SIZE);
		int id = 0;
		for(int i = 0; i < GYM_REGISTERED_CLIENTS; i++) {
			// The gym should generate clients randomly and have them execute their routines.
			id = r.nextInt(GYM_REGISTERED_CLIENTS) + 1; 
			if(clients.add(id)) {		// Check if random client ID is in the set
				Client client = Client.generateRandom(id);
				executor.execute(client);
			}	
		}
		executor.shutdown();
	}
	
}
