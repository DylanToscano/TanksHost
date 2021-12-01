package tiledMapObjects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import elements.BarrelEx;
import elements.Buff;
import elements.CooldownBuff;
import elements.Explosion;
import elements.ExplosiveBuff;
import elements.Hull;
import elements.Obstacle;
import elements.Projectile;
import elements.SpeedBuff;

public class WorldListener implements ContactListener {
	private Fixture fixA;
	private Fixture fixB;
	public Fixture actualLeft;
	public Fixture actualIn;
	public Fixture actualExplosion;

	@Override
	public void beginContact(Contact contact) {
		// el contacto genera entre 2 entidades, pero puede chocar mas de una entidad,
		// se generarian 2 BEGINcontact
		fixA = contact.getFixtureA();
		fixB = contact.getFixtureB();
		detectRoad();
		detectProjectil();
		detectBuff();
		detectBarrel();
		detectExplosion();
		detectObstacle();
	}
	// -------------------------END CONTACT------------------------------------

	@Override
	public void endContact(Contact contact) {
		
		if (fixA.getUserData() != null && fixB.getUserData() != null) {
			if (fixA.getUserData().equals("Road") || fixB.getUserData().equals("Road")) {

				Fixture road = (fixA.getUserData().equals("Road")) ? fixA : fixB;
				Fixture objeto = (road == fixA) ? fixB : fixA;

				if (objeto.getUserData() != null && (objeto.getUserData() instanceof Hull)) {
					
					// idk what happened really, but i thinks that the projectil counted as a hull(I
					// DONT KNOW WHY, SO I DID THIS TO NO REPEAT THE SAME ROAD unless
					// the tank reEnters that single road)
				
					if (((actualLeft == null || actualLeft != road)) || ((Hull) objeto.getUserData()).roadCounter > 0) {
						((Hull) objeto.getUserData()).outRoad();
					}
					actualLeft = road;

				}

			}
		}

	}



	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
	

	private void detectObstacle() {
		if (fixA.getUserData() instanceof Obstacle || fixB.getUserData() instanceof Obstacle) {
			Fixture obstacle = (fixA.getUserData() instanceof Obstacle) ? fixA : fixB;
			Fixture object = (obstacle == fixA) ? fixB : fixA;
			
			if (object.getUserData() != null && (object.getUserData() instanceof Buff)){
				
				((Obstacle) obstacle.getUserData()).needCorrect();	
				
				
			}
		}
		
		
	}



	private void detectExplosion() {
		
		if (fixA.getUserData() instanceof Explosion || fixB.getUserData() instanceof Explosion) {
			Fixture explosion = (fixA.getUserData() instanceof Explosion) ? fixA : fixB;
			Fixture object = (explosion == fixA) ? fixB : fixA;
			if (object.getUserData() != null && (object.getUserData() instanceof Hull)){
				if(explosion != actualExplosion) {
					((Hull) object.getUserData()).receiveExplosiveDamage(((Explosion) explosion.getUserData()).getDmg());
					actualExplosion = explosion;
				}
			}
		}
	}



	private void detectBarrel() {
		if (fixA.getUserData() instanceof BarrelEx || fixB.getUserData() instanceof BarrelEx) {
			Fixture barrel = (fixA.getUserData() instanceof BarrelEx) ? fixA : fixB;
			Fixture object = (barrel == fixA) ? fixB : fixA;
			
			if (object.getUserData() != null && (object.getUserData() instanceof Hull || object.getUserData() instanceof Projectile || object.getUserData() instanceof Explosion )) {
				
				((BarrelEx) barrel.getUserData()).Hitted();
			}
			if (object.getUserData() != null && (object.getUserData() instanceof Projectile)) {
				// the bullet just disappear
					((Projectile) object.getUserData()).explode();

			}
		}
	}



	private void detectProjectil() {
		if (fixA.getUserData() instanceof Projectile || fixB.getUserData() instanceof Projectile) {

			Fixture projectile = (fixA.getUserData() instanceof Projectile) ? fixA : fixB;
			Fixture object = (projectile == fixA) ? fixB : fixA;

			if (object.getUserData() != null && (object.getUserData() instanceof Hull)) {
				// trigger sensors

				if (((Hull) object.getUserData()) != ((Projectile) projectile.getUserData()).parent
						&& !((Projectile) projectile.getUserData()).isExploded()) {
					((Projectile) projectile.getUserData()).explode();
					((Hull) object.getUserData()).receiveDamage(((Projectile) projectile.getUserData()));

				}

			}
			if (object.getUserData() != null && object.getUserData() instanceof Obstacle) {
					((Projectile) projectile.getUserData()).explode();
			}
			System.out.println(object.getUserData());
			if (object.getUserData() != null && object.getUserData().equals("obstacle")) {
				((Projectile) projectile.getUserData()).explode();
		}
			
			
			

		}
	}

	private void detectRoad() {
		if (fixA.getUserData().equals("Road") || fixB.getUserData().equals("Road")) {
			Fixture road = (fixA.getUserData().equals("Road")) ? fixA : fixB;
			Fixture objeto = (road == fixA) ? fixB : fixA;
			if (objeto.getUserData() != null && (objeto.getUserData() instanceof Hull)) {
				// se activa la interaccion con el tipo de objeto que sea
				if ((actualIn == null || actualIn != road) || ((Hull) objeto.getUserData()).roadCounter == 0) {
					((Hull) objeto.getUserData()).inRoad();	
				}
				actualIn = road;
			}
		}
	}
	
	private void detectBuff() {
		if (fixA.getUserData() instanceof ExplosiveBuff || fixB.getUserData() instanceof ExplosiveBuff) {

			Fixture explosive = (fixA.getUserData() instanceof ExplosiveBuff) ? fixA : fixB;
			Fixture object = (explosive == fixA) ? fixB : fixA;

			if (object.getUserData() != null && (object.getUserData() instanceof Hull)) {
				// se activa la interaccion con el tipo de objeto que sea
				if (!((Hull) object.getUserData()).isBuffExplosive()) {
					((ExplosiveBuff) explosive.getUserData()).pick();
					((Hull) object.getUserData()).BuffExplosive();
				}
			}

		} else if (fixA.getUserData() instanceof SpeedBuff || fixB.getUserData() instanceof SpeedBuff) {

			Fixture speed = (fixA.getUserData() instanceof SpeedBuff) ? fixA : fixB;
			Fixture object = (speed == fixA) ? fixB : fixA;

			if (object.getUserData() != null && (object.getUserData() instanceof Hull)) {

				// se activa la interaccion con el tipo de objeto que sea

				if (!((Hull) object.getUserData()).isBuffSpeed()) {
					((SpeedBuff) speed.getUserData()).pick();
					((Hull) object.getUserData()).BuffSpeed();
				}

			}

		} else if (fixA.getUserData() instanceof CooldownBuff || fixB.getUserData() instanceof CooldownBuff) {

			Fixture cooldown = (fixA.getUserData() instanceof CooldownBuff) ? fixA : fixB;
			Fixture object = (cooldown == fixA) ? fixB : fixA;

			if (object.getUserData() != null && (object.getUserData() instanceof Hull)) {

				// se activa la interaccion con el tipo de objeto que sea
				if(!((Hull) object.getUserData()).isBuffCooldown()) {
					((CooldownBuff) cooldown.getUserData()).pick();
					((Hull) object.getUserData()).BuffCooldown();
				}
			}
		}

	}

}
