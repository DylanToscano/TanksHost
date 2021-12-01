package input;

import java.util.HashMap;
import java.util.Map;

import elements.Tank;

public class Client {
	
	public String username;
	public long firstTick;
	Tank tank;
	public Map<InputKeys, Boolean>  inputs = new HashMap<InputKeys, Boolean>();

	public Client() {
		initializeInputs();
	}
	
	protected void initializeInputs() {
		InputKeys[] array = InputKeys.values(); //Take all the enums InputKeys and put them in an array.
		for(int i=0; i<array.length;i++) { 
			inputs.put(array[i], false); //Use the array of enums to put each enum in the inputs list.
		}
	}
}
