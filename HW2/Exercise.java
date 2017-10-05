package HW2;

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
}
