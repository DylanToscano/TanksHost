package TankData;

import elements.Hull;
import elements.Tank;
import utilities.Resources;

public class BasicHull extends Hull{
	
	public BasicHull(Tank parent){
		//Sprite config
		super(parent, Resources.BASICHULL,100);
		
		//esto nose si quitarlo, lo comento preguntar despues
		hp = 300;
		
		
		//Specs
		
		rotationSpeed = 1f;
		slots = 1;
		
	}

}
