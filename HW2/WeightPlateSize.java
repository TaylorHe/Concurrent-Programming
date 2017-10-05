package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum WeightPlateSize{SMALL_3KG, MEDIUM_5KG, LARGE_10KG;
	
	/* 
	 * Found a better way that caches a list of values instead of making a new list
	 * every time when picking a random number from the list.
	 * 
	 * source: https://stackoverflow.com/questions/1972392/java-pick-a-random-value-from-an-enum
	 */
	private static final List<WeightPlateSize> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static WeightPlateSize randomWeightPlateSize() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
};