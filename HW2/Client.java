package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.ArrayList;
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
	 * @param noOfWeightPlates
	 * @return Client 
	 */
	public static Client generateRandom(int id, Map<WeightPlateSize, Integer> noOfWeightPlates){
		return null;
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
}
