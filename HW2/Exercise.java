package HW2;

import java.util.HashMap;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.Map;

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
		return null;
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
