package elements;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

import utilities.Config;
import utilities.Render;
import utilities.Resources;

public class Cannon extends Attachable {

	public boolean ready = true;
	public int reloadTime = 2;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	Sprite fireFX;
	public Sound fireSfx;
	public float time;
	public float fireEffectTime = 0.1f;

	Timer timer = new Timer();

	public Cannon(String idle, String fire) {
		super(idle);
		flip(false, true);
		fireFX = new Sprite(new Texture(Resources.CANNON_FIRE_FX));
		fireFX.flip(false, true);
		fireFX.setSize(getWidth(),getHeight());
		fireFX.setPosition(2*100,2*100);
		ready = false;
		objectType = "Cannon";
		
	}

	public void trigger() {
		if (time > reloadTime) {
			time = 0;
			ready = true;
//			fireSfx.play(1,Functions.randomFloat(0.8f,1.2f), 1);
			Projectile shell = new Projectile(getX() + getWidth() / 2, getY() + getHeight() / 2, hull,
					Resources.BASICSHELL, 3);
			fireFX.setOrigin(fireFX.getWidth()/2, -hull.getHeight()/1.5f);
			
			
			projectiles.add(shell);
		}
	}

	public void updateCannon() {
		time += Config.delta;

		for (int i = 0; i < projectiles.size(); i++) {
			if(projectiles.get(i).isExploded()) {
				
				projectiles.get(i).disappear();
				projectiles.remove(i);
			}else {
				projectiles.get(i).fired();
			}
			

		}
		

	}

	@Override
	public void update(float x, float y, float rotation) {
		super.update(x, y, rotation);
		fireFX.setPosition(x, y+hull.getHeight()/1.5f);
		fireFX.setRotation(rotation);
		if (time < fireEffectTime) {
			fireFX.draw(Render.batch);
		}
	}

}
