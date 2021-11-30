package utilities;

import java.util.Random;

public class Functions {
	
	
	public static float randomFloat(float min, float max) {
		Random r = new Random();
		float ra =  r.nextFloat();
		float random = min + ra * (max - min);
		
		return (float) random;
	}
	public static int randomInt(int min, int max) {
		Random r = new Random();
		int random = r.nextInt(max-min+1)+min;
		return random;
	}
}
