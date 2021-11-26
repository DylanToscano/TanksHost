package utilities;

import java.util.Random;

public class Functions {
	
	
	public static float randomFloat(float min, float max) {
		Random r = new Random();
		float random = min + r.nextFloat() * (max - min);
		return (float) random;
	}
	public static float randomInt(int min, int max) {
		Random r = new Random();
		int random = min + r.nextInt() * (max - min);
		return random;
	}
}
