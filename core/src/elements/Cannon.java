package elements;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import TankData.BasicShell;
import TankData.ExplosiveShell;
import utilities.Config;
import utilities.Render;
import utilities.Resources;

public class Cannon extends Attachable {
	public float reloadTime = 2;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	ClientSprite fireFX;
	public Sound fireSfx;
	public float time;
	public float fireEffectTime = 0.1f;

	public Cannon(String route) {
		super(route);
		flip(false, true);
		objectType = "Cannon";
	}



	public void update() {
		time += Config.delta;
		for (int i = 0; i < projectiles.size(); i++) {
			if (!projectiles.get(i).isExploded()) {
				projectiles.get(i).doMovement();
			}
		}

	}
	
	public void trigger() {
			Projectile shell;
			if(hull.isBuffExplosive()) {
				shell = new ExplosiveShell(getX() + getWidth() / 2, getY() + getHeight() / 2, hull);
			}else {
				shell = new BasicShell(getX() + getWidth() / 2, getY() + getHeight() / 2, hull);
			}
			projectiles.add(shell);
			fireFX = new ClientSprite(Resources.CANNON_FIRE_FX);
			Render.renderList.add(fireFX);
			fireFX.flip(false, true);
			fireFX.setSize(getWidth(), getHeight());
			fireFX.setPosition(2 * 100, 2 * 100);
			fireFX.setOrigin(fireFX.getWidth() / 2, -hull.getHeight() / 1.5f);
			Render.addSprite(shell);
			time = 0;

	}

	@Override
	public void update(float x, float y, float rotation) {
		super.update(x, y, rotation);
		fireFX.setPosition(x, y + hull.getHeight() / 1.5f);
		fireFX.setRotation(rotation);
		if (time > fireEffectTime) {
			Render.removeSprite(fireFX.getID()); //Yes, we can remove it directly with fireFX.remove();. No, we won't.
			
		}
	}
	public void buffFireRate(){
		reloadTime = reloadTime/2;
	}

}
