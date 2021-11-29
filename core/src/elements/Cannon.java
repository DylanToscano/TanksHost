package elements;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

import TankData.BasicShell;
import TankData.ExplosiveShell;
import utilities.Config;
import utilities.Render;
import utilities.Resources;

public class Cannon extends Attachable {
	public float reloadTime = 2;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	Sprite fireFX;
	public Sound fireSfx;
	public float time;
	public float fireEffectTime = 0.1f;

	public Cannon(String route) {
		super(route);
		flip(false, true);
		fireFX = new Sprite(new Texture(Resources.CANNON_FIRE_FX));
		fireFX.flip(false, true);
		fireFX.setSize(getWidth(), getHeight());
		fireFX.setPosition(2 * 100, 2 * 100);
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
//			fireSfx.play(1,Functions.randomFloat(0.8f,1.2f), 1);
			if(hull.isBuffExplosive()) {
				shell = new ExplosiveShell(getX() + getWidth() / 2, getY() + getHeight() / 2, hull);
			}else {
				shell = new BasicShell(getX() + getWidth() / 2, getY() + getHeight() / 2, hull);
			}
			
			Render.addSprite(shell);
			fireFX.setOrigin(fireFX.getWidth() / 2, -hull.getHeight() / 1.5f);
			projectiles.add(shell);
			time = 0;

	}

	@Override
	public void update(float x, float y, float rotation) {
		super.update(x, y, rotation);
		fireFX.setPosition(x, y + hull.getHeight() / 1.5f);
		fireFX.setRotation(rotation);
		
		if (time < fireEffectTime) {
			Render.batch.begin();
			fireFX.draw(Render.batch);
			Render.batch.end();
		}
	}
	public void buffFireRate(){
		reloadTime = reloadTime/2;
	}

}
