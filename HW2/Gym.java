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
	static Map<WeightPlateSize, Integer> remainingNoOfWeightPlates; // Used to calculate 
	private Set<Integer> clients;
	private ExecutorService executor;
	private Random r;
	// There are only 5 of each gym apparatus type
	// I wish there were a less verbose way to initialize these objects
	final static Semaphore LEGPRESSMACHINE = new Semaphore(5);
	final static Semaphore BARBELL = new Semaphore(5);
	final static Semaphore HACKSQUATMACHINE = new Semaphore(5);
	final static Semaphore LEGEXTENSIONMACHINE = new Semaphore(5);
	final static Semaphore LEGCURLMACHINE = new Semaphore(5);
	final static Semaphore LATPULLDOWNMACHINE = new Semaphore(5);
	final static Semaphore PECDECKMACHINE = new Semaphore(5);
	final static Semaphore CABLECROSSOVERMACHINE = new Semaphore(5);
	
	

	
	public Gym() {
		noOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
		remainingNoOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
		r = new Random();
		clients = new HashSet<Integer>();
	}
	
	
	public void run(){
		// Thread pool
		executor = Executors.newFixedThreadPool(GYM_SIZE);
		int id = 0, numExercises = 0;
		for(int i = 0; i < GYM_REGISTERED_CLIENTS; i++) {
			// The gym should generate clients randomly and have them execute their routines.
			id = r.nextInt(GYM_REGISTERED_CLIENTS) + 1;
			// Check if random client ID is in the set
			if(clients.add(id)) {
				Client client = new Client(id);
				numExercises = r.nextInt(5) + 15; // The client should have between 15 and 20 exercises in their routine.
				for(int newExercise = 0; newExercise < numExercises; newExercise++) {
					client.getRoutine().add(Exercise.generateRandom(noOfWeightPlates));
				}
				executor.execute(client);
			}	
		}
		executor.shutdown();
	}
	
}
