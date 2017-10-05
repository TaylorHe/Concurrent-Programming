package HW2;

import java.util.List;
import java.util.Map;

public class Client {
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
	 * @param noOfWeightPlates
	 * @return Client 
	 */
	public static Client generateRandom(int id, Map<WeightPlateSize, Integer> noOfWeightPlates){
		return null;
	}
	
}
