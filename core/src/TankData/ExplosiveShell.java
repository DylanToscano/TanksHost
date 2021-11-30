package TankData;

import elements.Explosion;
import elements.Hull;
import elements.Projectile;
import utilities.ClientRender;
import utilities.Resources;

public class ExplosiveShell extends Projectile{
	float ExplosiveDmg;
	public ExplosiveShell(float x, float y, Hull hull) {
		super(x, y, hull, Resources.EXPLOSIVESHELL, 2);
		dmg = 50;
		
	
	}
	public float getExplosiveDmg() {
		return ExplosiveDmg;
	}
	
	@Override
	public void explode() {
		super.explode();
		Explosion explosion = new Explosion(getX(),getY());
		ClientRender.addSprite(explosion);
	}
	
}
