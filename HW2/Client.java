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
	 * 
	 * @param weightMap
	 * @param at The apparatus type
	 * @param e The exercise
	 */
	private void acquireAndExercise(Map<WeightPlateSize, Integer> weightMap, ApparatusType at, Exercise e, int routineNum){
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
				Gym.allPlateAccess.acquire();
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
				Gym.sPlateAccess.release();
				Gym.mPlateAccess.release();
				Gym.lPlateAccess.release();
				Gym.allPlateAccess.release();
			}
			// Acquire each weight and modify Gym.remaingNoOfWeights per acquire. 
			try {
				for(int i = 0; i < numSmallWeights; i++) {
					Gym.sPlateMutex.acquire();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, --numRemainWeights[0]);
				}
				Gym.sPlateAccess.release(); // Release the access when done.
				for(int i = 0; i < numMedWeights; i++) {
					Gym.mPlateMutex.acquire();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, --numRemainWeights[1]);
				}
				Gym.mPlateAccess.release();
				for(int i = 0; i < numLargeWeights; i++) {
					Gym.lPlateMutex.acquire();
					Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, --numRemainWeights[2]);
				}
				//System.out.println("Client " + id + " leaves: " + numRemainWeights[0] + ", " + numRemainWeights[1] + ", " + numRemainWeights[2]);
				Gym.lPlateAccess.release();
				Gym.allPlateAccess.release();
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			System.out.println("Client #" + id + " will use " + at + " for " + e.getDuration() + " minutes. Routine " + routineNum + " out of " +routine.size() + ".");
			//System.out.println("Resources left: " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG));
			
			Thread.sleep(e.getDuration());
			System.out.println("Client #" + id + " finished using " + at + " after " + e.getDuration() + " minutes.");
			//System.out.println("stuck here1");
			//Gym.allPlateAccess.acquire();
			//System.out.println("stuck here1.5");
			Gym.sPlateAccess.acquire();
			int numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG);
			for(int i = 0; i < numSmallWeights; i++) {
				Gym.sPlateMutex.release();
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.SMALL_3KG, numRemain + numSmallWeights);
			
			Gym.sPlateAccess.release();
			//System.out.println("stuck here2");
			
			Gym.mPlateAccess.acquire();
			numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG); 
			for(int i = 0; i < numMedWeights; i++) {
				Gym.mPlateMutex.release();	
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, numRemain + numMedWeights);
			
			Gym.mPlateAccess.release();
			//System.out.println("stuck here3");
			
			Gym.lPlateAccess.acquire();
			numRemain = Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG);
			for(int i = 0; i < numLargeWeights; i++) {
				Gym.lPlateMutex.release();
			}
			Gym.remainingNoOfWeightPlates.put(WeightPlateSize.LARGE_10KG, numRemain + numLargeWeights);
			Gym.lPlateAccess.release();
			//Gym.allPlateAccess.release();
			
			//System.out.println("Resources left: " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.SMALL_3KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.MEDIUM_5KG) + ", " + Gym.remainingNoOfWeightPlates.get(WeightPlateSize.LARGE_10KG));
					
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
			Map<WeightPlateSize, Integer> weightMap = e.getWeightPlateSizeMap();
			// Now we need a switch when attempting to acquire each machine.
			routineNumber++;
			ApparatusType a = e.getApparatusType();
			switch (a) {
			case LEGPRESSMACHINE:
				try {
					Gym.LEGPRESSMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.LEGPRESSMACHINE.release();
				break;

			case BARBELL:
				try {
					Gym.BARBELL.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.BARBELL.release();
				break;

			case HACKSQUATMACHINE:
				try {
					Gym.HACKSQUATMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.HACKSQUATMACHINE.release();
				break;

			case LEGEXTENSIONMACHINE:
				try {
					Gym.LEGEXTENSIONMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.LEGEXTENSIONMACHINE.release();
				break;

			case LEGCURLMACHINE:
				try {
					Gym.LEGCURLMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.LEGCURLMACHINE.release();
				break;

			case LATPULLDOWNMACHINE:
				try {
					Gym.LATPULLDOWNMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.LATPULLDOWNMACHINE.release();
				break;

			case PECDECKMACHINE:
				try {
					Gym.PECDECKMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.PECDECKMACHINE.release();
				break;

			case CABLECROSSOVERMACHINE:
				try {
					Gym.CABLECROSSOVERMACHINE.acquire();
					acquireAndExercise(weightMap, a, e, routineNumber);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Gym.CABLECROSSOVERMACHINE.release();
				break;
			}
		}


	}
}
