package TankData;

import elements.Hull;
import utilities.Resources;

public class BasicHull extends Hull{
	
	public BasicHull(){
		//Sprite config
		super(Resources.BASICHULL,100);
		
		//esto nose si quitarlo, lo comento preguntar despues
		
		startRotation = 90;
		
		//Specs
		
		rotationSpeed = 0.5f;
		slots = 1;
		
	}

}
