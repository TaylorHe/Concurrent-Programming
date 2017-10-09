package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

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
	 * 
	 * @param weightMap
	 * @param at The apparatus type
	 * @param e The exercise
	 */
	private void acquireWeightsAndExercise(Exercise e, int routineNum) {
		Map<WeightPlateSize, Integer> weightMap = e.getWeightPlateSizeMap();
		try {
			// Number of weights needed
			int numSmallWeights = weightMap.get(WeightPlateSize.SMALL_3KG);
			int numMedWeights = weightMap.get(WeightPlateSize.MEDIUM_5KG);
			int numLargeWeights = weightMap.get(WeightPlateSize.LARGE_10KG);
			int numRemainWeights[] = new int[3];
			// Check if there are enough plates. Must check while blocking access to plate modification.
			boolean hasEnoughWeights = false;
			//System.out.println("Client " + id + "is requesting resource: " + numSmallWeights + ", " + numMedWeights + ", " + numLargeWeights);

			while(true){
				Gym.sPlateAccess.acquire();
				Gym.mPlateAccess.acquire();
				Gym.lPlateAccess.acquire();
				numRemainWeights[0] = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG);
				numRemainWeights[1] = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG);
				numRemainWeights[2] = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG);
				hasEnoughWeights =
						numSmallWeights <= numRemainWeights[0] &&
						numMedWeights   <= numRemainWeights[1] &&
						numLargeWeights <= numRemainWeights[2];
				if (hasEnoughWeights){
					break;
				} 
				// If we don't have enough, we release try again.
				Gym.sPlateAccess.release();
				Gym.mPlateAccess.release();
				Gym.lPlateAccess.release();
			}
			// Acquire each weight and modify Gym.remaingNoOfWeights per acquire. 
			try {
				for(int i = 0; i < numSmallWeights; i++) {
					Gym.sPlates.acquire();
				}
				Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, numRemainWeights[0] - numSmallWeights);
				Gym.sPlateAccess.release(); // Release the access when done.
				
				for(int i = 0; i < numMedWeights; i++) {
					Gym.mPlates.acquire();
				}
				Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, numRemainWeights[1] - numMedWeights);
				Gym.mPlateAccess.release();
				
				for(int i = 0; i < numLargeWeights; i++) {
					Gym.lPlates.acquire();
				}
				Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, numRemainWeights[2] - numLargeWeights);
				Gym.lPlateAccess.release();
				//System.out.println("Client " + id + " leaves: " + numRemainWeights[0] + ", " + numRemainWeights[1] + ", " + numRemainWeights[2]);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// The client "uses" the machine
			System.out.println("Client #" + id + " will use " + e.getApparatusType() + " for " + e.getDuration() + " minutes. Routine " + routineNum + " out of " +routine.size() + ".");
			//System.out.println("Resources left: " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG));
			Thread.sleep(e.getDuration());
			System.out.println("Client #" + id + " finished using " + e.getApparatusType() + " after " + e.getDuration() + " minutes.");

			// After the client is done, we have to put the plates back
			Gym.sPlateAccess.acquire();
			int numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG);
			for(int i = 0; i < numSmallWeights; i++) {
				Gym.sPlates.release();
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, numRemain + numSmallWeights);

			Gym.sPlateAccess.release();

			Gym.mPlateAccess.acquire();
			numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG); 
			for(int i = 0; i < numMedWeights; i++) {
				Gym.mPlates.release();	
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, numRemain + numMedWeights);

			Gym.mPlateAccess.release();

			Gym.lPlateAccess.acquire();
			numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG);
			for(int i = 0; i < numLargeWeights; i++) {
				Gym.lPlates.release();
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, numRemain + numLargeWeights);
			Gym.lPlateAccess.release();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public void run() {
		// For each exercise in the routine, 
		//     1. Acquire the permission for the machine
		//     2. Acquire the weights, exercise, and release the weights
		//     3. We should also remove the id from the client set since it is done
		int routineNumber = 0;
		for(Exercise e : routine){
			// Now we need a switch when attempting to acquire each machine.
			// EDIT: No more switch and stupid copy-paste code, I make a HashMap instead of <at,Semaphore>
			routineNumber++;
			Semaphore machine = Gym.apparatusTypeSemaphoreMap.get(e.getApparatusType());
			try {
				machine.acquire();
				acquireWeightsAndExercise(e, routineNumber);
				machine.release();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
