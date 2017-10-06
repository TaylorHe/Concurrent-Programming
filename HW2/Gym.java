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
	private Map<WeightPlateSize, Integer> noOfWeightPlates;
	private static Map<WeightPlateSize, Integer> tempNoOfWeightPlates; // exists so that it can be used as a static getter
	static Map<WeightPlateSize, Integer> remainingNoOfWeightPlates; // Used to calculate 
	static Map<Integer, ApparatusType> apparatusMap = new HashMap<Integer, ApparatusType>();
	private Set<Integer> clients;
	private ExecutorService executor;
	private Random r;
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
	final static Semaphore plateMutex= new Semaphore(1);
	final static Semaphore lPlateMutex= new Semaphore(75);
	final static Semaphore mPlateMutex= new Semaphore(90);
	final static Semaphore sPlateMutex= new Semaphore(110);
	
	
	public Gym() {
		noOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
		tempNoOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		tempNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		tempNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		tempNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
		remainingNoOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
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
			// Check if random client ID is in the set
			if(clients.add(id)) {
				Client client = Client.generateRandom(id);
				executor.execute(client);
			}	
		}
		executor.shutdown();
	}
	
}
