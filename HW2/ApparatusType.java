package HW2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum ApparatusType {
	LEGPRESSMACHINE, BARBELL, HACKSQUATMACHINE, LEGEXTENSIONMACHINE, 
	LEGCURLMACHINE, LATPULLDOWNMACHINE, PECDECKMACHINE,
	CABLECROSSOVERMACHINE;
	
	private static final List<ApparatusType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static ApparatusType randomApparatusType() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
