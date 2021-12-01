package elements;

import TankData.BasicCannon;
import TankData.BasicHull;
import input.Client;
import input.InputKeys;
import utilities.Config;
import utilities.Render;

public class Tank implements Updateable{
	public Hull hull;
	public Client owner;
	boolean forward; // direction. true if going forward, false if reverse.
	float time;
	public float tempX = 0;
	public float tempY = 0;
	public float correctionX,correctionY;
	public int id;
	
	public boolean correction;
	// Array holding other elements of the tank, such as the cannon.
	Attachable[] objects;

	public Tank(Client player) {
		// TODO SETEAR LA POSICION DESDE ACA
		owner = player;
		this.hull = new BasicHull(this);
		objects = new Attachable[hull.slots];
		attach(new BasicCannon());
		Render.updateList.add(this);
		Render.tanks.add(this);
		Render.addSprite(hull);
	}

	public void update() {
		time += Config.delta;
		doMovement();
		doCannon();
		updateObjects();// Update other sprites attached to this tank, such as cannon.
	}

	public void setPosition(float x, float y) {
		hull.setPosition(x, y);
	}

	public void setRotation(float rotation) {
		hull.rotation = rotation;
		hull.setRotation(rotation);
	}

	//// movement functions
	private void doMovement() {
		doRotation();

		tempX = (float) Math.sin(Math.toRadians(hull.rotation));
		tempY = (float) Math.cos(Math.toRadians(hull.rotation));
		if (hull.roadCounter == 0) {
			tempX = tempX / 2;
			tempY = tempY / 2;
		}
		if (hull.isBuffSpeed()) {
			tempX = tempX * 1.4f;
			tempY = tempY * 1.4f;
		}

//		hull.
		if (owner.inputs.get(InputKeys.UP) && !owner.inputs.get(InputKeys.DOWN)) { // If pressing W, go forward.

			hull.moveHull(-tempX, tempY);
		} else if (owner.inputs.get(InputKeys.DOWN) && !owner.inputs.get(InputKeys.UP)) { // If pressing S, go reverse
			hull.moveHull(tempX / 1.5f, -tempY / 1.5f);
		} else {
			hull.stopHull();
		}

		setPosition((hull.b2body.getPosition().x - hull.getWidth() / 2), // before this, doMovement only sets the
				hull.b2body.getPosition().y - hull.getHeight() / 2); // body2d(the rectangle) and here updates the
																		// sprite

	}

	private void doRotation() {
		if (owner.inputs.get(InputKeys.RIGHT)) {
			rotate(hull.rotationSpeed * -1);
		}

		if (owner.inputs.get(InputKeys.LEFT)) {
			rotate(hull.rotationSpeed);
		}
	}

	public void rotate(float degrees) {
		hull.rotate(degrees);
		hull.rotation += degrees;
		
		if (hull.rotation >= 360) {
			hull.rotation = 0;
		} else if (hull.rotation <= 0) {
			hull.rotation = 359;
		}
		hull.setRotation(hull.rotation);
	}

	///////////// Cannon-related functions.

	private void doCannon() {
		if (owner.inputs.get(InputKeys.FIRE)) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i].objectType == "Cannon") {// failsafe from
					if (time > ((Cannon) objects[i]).reloadTime) {// spamming space
						time = 0;
						((Cannon) objects[i]).trigger(); // omg
					}
				}
			}
		}
	}

	///////////// functions related to attached objects
	public void attach(Attachable object) {
		int availablePos = -1;
		for (int i = 0; i < objects.length; i++) { // Find an available position in the array.
			if (availablePos == -1 && objects[i] == null) {
				availablePos = i;
			}
		}
		if (availablePos != -1) {
			objects[availablePos] = object;

			object.hull = this.hull;
		} // If a place was found, attach the object.
	}

	void updateObjects() { // Fix attached objects position & rotation
		//
		for (int i = 0; i < objects.length; i++) {
//			if (objects[i].objectType.equals("Cannon")) { // updates cannon a its respective projectiles
//				((Cannon) objects[i]).update();
//			}
			if (objects[i] != null) {
				objects[i].update(hull.getX() + hull.getWidth() / 2 - objects[i].getWidth() / 2,
						hull.getY() + hull.getHeight() / 2, hull.rotation);
				// TODO look a this, when added more attachables, cause its only tought to have
				// 1 attachable

			}

		}

	}

	public void destroy() {
		Explosion explosion = new Explosion(hull.getX(),hull.getY());
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				objects[i].remove();
			}
		}
		
		for (int i = 0; i < Render.renderList.size(); i++) {
			if(Render.tanks.get(i) == this) {
				Render.tanks.remove(i);
				break;
			}
		}
		
		hull.remove();
		hull.disappear();

	}

}
