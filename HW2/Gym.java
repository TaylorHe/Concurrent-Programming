package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Gym implements Runnable {
	private static final int GYM_SIZE = 30;
	private static final int GYM_REGISTERED_CLIENTS = 10000;
	private Map<WeightPlateSize, Integer> noOfWeightPlates;
	private Set<Integer> clients;
	private ExecutorService executor;
	
	
	public Gym() {
		noOfWeightPlates = new HashMap<WeightPlateSize, Integer>();
		noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
		noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
		noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
		
	}
	public void run(){
		//TODO
	}
	
}
