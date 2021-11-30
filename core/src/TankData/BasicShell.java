package TankData;

import elements.Hull;
import elements.Projectile;
import utilities.Resources;

public class BasicShell extends Projectile{
	public BasicShell(float x, float y, Hull hull) {
		super(x, y, hull, Resources.BASICSHELL, 2);
		dmg = 100;
	}

	
}
