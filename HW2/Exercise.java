package HW2;

import java.util.HashMap;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.Map;
import java.util.Random;

public class Exercise {
	private ApparatusType at;
	private Map<WeightPlateSize, Integer> weight;
	private int duration;
	/**
	 * Constructor for Exercise object.
	 * @param at
	 * @param weight
	 * @param duration
	 */
	public Exercise(ApparatusType at, Map<WeightPlateSize, Integer> weight, int duration){
		this.at = at;
		this.weight = weight;
		this.duration = duration;
	}
	/**
	 * Generates a random exercise to be performed.
	 * @return Exercise
	 */
	public static Exercise generateRandom(Map<WeightPlateSize, Integer> weight){
		Map<WeightPlateSize, Integer> tempWeight = new HashMap<WeightPlateSize, Integer>();
		Random r = new Random();
		// For every key in Map tempWeight, we must generate a random Integer
		for(WeightPlateSize p : tempWeight.keySet()){
			// For non-zero random, we can random from 0 to size-1 and then add 1
			// r.nextInt is exclusive, so we don't need a -1
			tempWeight.put(p, r.nextInt(weight.get(p)) + 1);
		}
		//The duration of each exercise should be anything reasonable, say 25
		int tempDuration = r.nextInt(25) + 1;
		
		return new Exercise(ApparatusType.randomApparatusType(), tempWeight, tempDuration);
	}

	/**
	 * Returns the Apparatus Type
	 * @return ApparatusType
	 */
	public ApparatusType getApparatusType() {
		return at;
	}
	
	/**
	 * Returns the weight plate sizes
	 * @return Map<WeightPlateSize,Integer>
	 */
	public Map<WeightPlateSize,Integer> getWeightPlateSize() {
		return weight;
	}
	
	/**
	 * Returns the duration of the routine
	 * @return int
	 */
	public int getDuration() {
		return duration;
	}

}
