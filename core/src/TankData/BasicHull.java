package TankData;

import elements.Hull;
import elements.Tank;
import utilities.Resources;

public class BasicHull extends Hull{
	
	public BasicHull(Tank parent){
		super(parent, Resources.BASICHULL,100);
		hp = 300;
		rotationSpeed = 1f;
		slots = 1;
		
	}

}
