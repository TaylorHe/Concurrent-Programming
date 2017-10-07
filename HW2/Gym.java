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
	
	private Map<WeightPlateSize, Integer> noOfWeightPlates;
	static Map<WeightPlateSize, Integer> remainingNoOfWeightPlates; // Used to calculate remaining plates 
	// tempNoOfWeightPlates exists so that it can be used as a static getter for Client.generateRandom. It's just a copy of noOfWeightPlates
	private static Map<WeightPlateSize, Integer> tempNoOfWeightPlates; 
	
	private Set<Integer> clients;
	private ExecutorService executor;
	private Random r;
	static Map<Integer, ApparatusType> apparatusMap = new HashMap<Integer, ApparatusType>();
	// There are only 5 of each gym apparatus type
	final static Semaphore LEGPRESSMACHINE = new Semaphore(5);
	final static Semaphore BARBELL = new Semaphore(5);
	final static Semaphore HACKSQUATMACHINE = new Semaphore(5);
	final static Semaphore LEGEXTENSIONMACHINE = new Semaphore(5);
	final static Semaphore LEGCURLMACHINE = new Semaphore(5);
	final static Semaphore LATPULLDOWNMACHINE = new Semaphore(5);
	final static Semaphore PECDECKMACHINE = new Semaphore(5);
	final static Semaphore CABLECROSSOVERMACHINE = new Semaphore(5);
	// Binary semaphore used to acquire the permissions to get plates.
	final static Semaphore lPlateAccess= new Semaphore(1);
	final static Semaphore mPlateAccess= new Semaphore(1);
	final static Semaphore sPlateAccess= new Semaphore(1);
	final static Semaphore lPlateMutex= new Semaphore(NUM_LARGE_PLATES);
	final static Semaphore mPlateMutex= new Semaphore(NUM_MEDIUM_PLATES);
	final static Semaphore sPlateMutex= new Semaphore(NUM_SMALL_PLATES);
	final static Semaphore allPlateAccess= new Semaphore(1);
	
	public Gym() {
		noOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, NUM_LARGE_PLATES);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, NUM_MEDIUM_PLATES);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, NUM_SMALL_PLATES);
		
		remainingNoOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, NUM_LARGE_PLATES);
		remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, NUM_MEDIUM_PLATES);
		remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, NUM_SMALL_PLATES);
		
		tempNoOfWeightPlates = noOfWeightPlates;
		
		r = new Random();
		clients = new HashSet<Integer>();
	}
	
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
