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
	public static Client generateRandom(int id) {
		Client client = new Client(id);
		Random r = new Random();
		int numExercises = r.nextInt(5) + 15; // The client should have between 15 and 20 exercises in their routine.
		for(int newExercise = 0; newExercise < numExercises; newExercise++) {
			client.addExercise(Exercise.generateRandom(Gym.getNoOfWeightPlates()));
		}
		return client;
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

	/**
	 * 
	 * @param weightMap
	 * @param at The apparatus type
	 * @param e The exercise
	 */
	private void acquireAndExercise(Map<WeightPlateSize, Integer> weightMap, ApparatusType at, Exercise e){
		try {
			// Acquire the mutex to modify Gym.remainingNoOfWeightPlates
			Gym.plateMutex.acquire();
			int numSmallWeights = weightMap.get(WeightPlateSize.SMALL_3KG);
			int numMedWeights = weightMap.get(WeightPlateSize.SMALL_3KG);
			int numLargeWeights = weightMap.get(WeightPlateSize.SMALL_3KG);
			
			int numRemainWeights[] = {Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG),
					Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG),
					Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG)};
			// Check if there are enough plates.
			if (numSmallWeights <= numRemainWeights[0] &&
					numMedWeights   <= numRemainWeights[1] &&
					numLargeWeights <= numRemainWeights[2]) {
				// Acquire each weight and modify Gym.remaingNoOfWeights per acquire
				try {
					for(int i = 0; i < numSmallWeights; i++) {
						Gym.sPlateMutex.acquire();
						Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, numRemainWeights[0]--);
					}
					for(int i = 0; i < numMedWeights; i++) {
						Gym.mPlateMutex.acquire();
						Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, numRemainWeights[1]--);
					}
					for(int i = 0; i < numLargeWeights; i++) {
						Gym.lPlateMutex.acquire();
						Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, numRemainWeights[2]--);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				System.out.println("Client #" + id + " will use " + at + " for " + e.getDuration() + " minutes.");
				Thread.sleep(e.getDuration());
				System.out.println("Client #" + id + " finished using " + at + " after " + e.getDuration() + " minutes.");

				// Release all the weights
				for(int i = 0; i < numSmallWeights; i++) {
					Gym.sPlateMutex.release();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, numRemainWeights[0]++);
				}
				for(int i = 0; i < numMedWeights; i++) {
					Gym.mPlateMutex.release();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, numRemainWeights[1]++);
				}
				for(int i = 0; i < numLargeWeights; i++) {
					Gym.lPlateMutex.release();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, numRemainWeights[2]++);
				}
				
				Gym.plateMutex.release();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public void run() {
		// For each exercise in the routine, 
		//     1. Acquire the permission for the machine
		//     2. Acquire the weights, exercise, and release the weights
		//     3. We should also remove the id from the client set
		
		for(Exercise e : routine){
			Map<WeightPlateSize, Integer> weightMap = e.getWeightPlateSizeMap();
			// Now we need a switch when attempting to acquire each machine.
			ApparatusType a = e.getApparatusType();
			switch (a) {
			case LEGPRESSMACHINE:
				try {
					Gym.LEGPRESSMACHINE.acquire();
					acquireAndExercise(weightMap, a, e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;
			}




		}


	}
}
