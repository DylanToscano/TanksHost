package tiledObjects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import elements.Hull;
import elements.Projectile;

public class WorldListener implements ContactListener {
	private Fixture fixA;
	private Fixture fixB;
	public Fixture actual;
	private int roadCounter; // im TOOOOOOOOO LAZY
	// basically it will count how many roads the tanks has been touching, when he
	// left the road discounts 1
	// when the hull left a road it will look if the roadCounter is 0, else it wont
	// trigger "hull.outRoad"

	@Override
	public void beginContact(Contact contact) {
		// el contacto genera entre 2 entidades, pero puede chocar mas de una entidad,
		// se generarian 2 BEGINcontact
		fixA = contact.getFixtureA();
		fixB = contact.getFixtureB();

		if (fixA.getUserData().equals("Road") || fixB.getUserData().equals("Road")) {

			Fixture Road = (fixA.getUserData().equals("Road")) ? fixA : fixB;
			Fixture objeto = (Road == fixA) ? fixB : fixA;

			if (objeto.getUserData() != null && (objeto.getUserData() instanceof Hull)) {
				roadCounter += 1;
				System.out.println(roadCounter);
				// se activa la interaccion con el tipo de objeto que sea
				((Hull) objeto.getUserData()).inRoad();

			}

		}
		if (fixA.getUserData() instanceof Projectile || fixB.getUserData() instanceof Projectile) {

			Fixture projectile = (fixA.getUserData() instanceof Projectile) ? fixA : fixB;
			Fixture hull = (projectile == fixA) ? fixB : fixA;

			if (hull.getUserData() != null && (hull.getUserData() instanceof Hull)) {
				// trigger sensors

				if (((Hull) hull.getUserData()) != ((Projectile) projectile.getUserData()).parent
						&& !((Projectile) projectile.getUserData()).isExploded()) {

					((Projectile) projectile.getUserData()).explode();
					((Hull) hull.getUserData()).receiveDamage(((Projectile) projectile.getUserData()));

				}

			}

		}

	}

	// -------------------------END CONTACT------------------------------------

	@Override
	public void endContact(Contact contact) {
		if (fixA.getUserData() != null && fixB.getUserData() != null) {
			if ( fixA.getUserData().equals("Road") || fixB.getUserData().equals("Road")) {

				Fixture Road = (fixA.getUserData().equals("Road")) ? fixA : fixB;
				Fixture objeto = (Road == fixA) ? fixB : fixA;

				if (objeto.getUserData() != null && (objeto.getUserData() instanceof Hull)) {
					// se activa la interaccion con el tipo de objeto que sea
					roadCounter --;
					System.out.println(roadCounter);
					if (roadCounter == 0) {
						
						((Hull) objeto.getUserData()).outRoad();
					}
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

}
