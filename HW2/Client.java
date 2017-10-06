package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Client implements Runnable {
	// Every client has an id
	private int id;
	// A routine is a list of Exercises
	private List<Exercise> routine;
	
	/**
	 * Client constructor
	 * @param id
	 */
	public Client(int id){
		this.id = id;
		routine = new ArrayList<Exercise>();
	}
	
	/**
	 * adds an Exercise to Client object
	 * @param e
	 * @return
	 */
	public void addExercise(Exercise e){
		this.routine.add(e);
	}
	/**
	 * Randomly creates a Client id and routine.
	 * @param id
	 * @return Client 
	 */
	public static Client generateRandom(int id){
		// Not sure what this is used for, but it's needed in to UML diagram.
		return new Client(id);
	}
	
	/**
	 * Returns the Client's id
	 * @return int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the Client's routine
	 * @return List<Exercise>
	 */
	public List<Exercise> getRoutine() {
		return routine;
	}

	private void acquireAndExercise(int numSmallWeights, int numMedWeights, int numLargeWeights){
		// Check if there are enough plates.
		if(numSmallWeights <= Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG)
			&& numMedWeights <= Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG)
			&& numLargeWeights <= Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG)) {
			
		}
		
	}
	@Override
	public void run() {
		int numRoutines = routine.size();
		for(int i = 0; i < numRoutines; i++){
			Exercise e = routine.get(i);
			Map<WeightPlateSize, Integer> weights = e.getWeightPlateSize();
			// Now we need a switch when attempting to acquire each machine.
			switch (e.getApparatusType()){
			case LEGPRESSMACHINE:
				try {
					Gym.LEGPRESSMACHINE.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					
				}
			}
			
			
		}
		
		
	}
}
