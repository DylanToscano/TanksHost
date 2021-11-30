package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import input.InputKeys;
import utilities.Config;
import utilities.Render;

public class Hull extends Entidad2D {
	public int id;
	private World world;
	public int roadCounter = 0;
	private boolean[] buffs = { false, false, false }; // speed, fire cooldown, explosiveShell
	public int startRotation;
	public Tank parent;
	// stats
	protected float hp = 0;
	protected float hpTotal = 0;
	public boolean onRoad;
	public float rotation; // degrees
	public int weaponSlots;
	public float rotationSpeed;
	public int slots;

	public Hull(Tank parent, String texture, int hp) {
		super(new Texture(texture));
		this.hp = hp;
		this.hpTotal = hp;
		this.world = Render.world;
		this.parent = parent;
		setSize(getWidth() / 2 / Config.PPM, getHeight() / 2 / Config.PPM);
		setOrigin(getWidth() / 2, getHeight() / 2);
		switch (Render.tanks.size()) {
		case 0:
			setPosition(2 * 15 / Config.PPM, 2 * 15 / Config.PPM);
			break;
		case 1:
			setPosition(30 * 15 / Config.PPM, 30 * 15 / Config.PPM); // TODO testing, change from x, 30 -> 2 and y 30
																		// ->62
			break;
		case 2:
			setPosition(62 * 15 / Config.PPM, 2 * 15 / Config.PPM);
			break;
		case 3:
			setPosition(62 * 15 / Config.PPM, 62 * 15 / Config.PPM);
			break;
		default:
			setPosition(1, 1);
			break;
		}
		createBody();
		fixtureDef();

	}

	protected void createBody() {
		// new Body
		bdef = new BodyDef();
		// starter position of body
		bdef.position.set(getX(), getY());
		// kind of body and set the map
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

	}

	protected void fixtureDef() {
		fdef = new FixtureDef();
		// defines what kind the box is going to have
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth() / 2, getHeight() / 2);
		fdef.filter.categoryBits = Config.TANK_BIT;
		// definimos la mascara de bits, que objetos box2d tiene que darle atencion.
		fdef.filter.maskBits = Config.DEFAULT_BIT | Config.ROAD_BIT | Config.TANK_BIT | Config.EXPLOSION_BIT
				| Config.PROJECTIL_BIT | Config.BUFF_BIT | Config.BARREL_BIT;
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
	}

	public void setVelocidad(float x, float y) {
		b2body.setLinearVelocity(x, y);
	}

	public void inRoad() {
		roadCounter++;
	}

	public void outRoad() {
		roadCounter--;
	}

	public boolean isOnRoad() {
		return onRoad;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void BuffSpeed() {
		rotationSpeed = rotationSpeed * 1.5f;
		buffs[0] = true;
	}

	public void BuffCooldown() {
		for (int i = 0; i < parent.objects.length; i++) {
			if (parent.objects[i].objectType == "Cannon") {
				((Cannon) parent.objects[i]).buffFireRate();
			}
		}
		buffs[1] = true;
	}

	public void BuffExplosive() {
		buffs[2] = true;
	}

	public boolean isBuffSpeed() {
		return buffs[0];
	}

	public boolean isBuffCooldown() {
		return buffs[1];
	}

	public boolean isBuffExplosive() {
		return buffs[2];
	}

	public int getWeaponSlots() {
		return weaponSlots;
	}

	public void setWeaponSlots(int weaponSlots) {
		this.weaponSlots = weaponSlots;
	}

	public void moveHull(float x, float y) {

		b2body.setLinearVelocity(x, y);
	}

	public void stopHull() {
		b2body.setLinearVelocity(0, 0);
	}

	public void receiveExplosiveDamage(float dmg) {
		hp -= dmg;
		hp = (hp < 0) ? 0 : hp;
		System.out.println("hp despueps de expl: " + hp);
	}

	public void receiveDamage(Projectile p) {
		float angle = 0;
		angle = calculateAngle(p);
		if (angle < 45 || angle > 315) {
			System.out.println("arriba");
			hp -= p.dmg * 0.75;
		} else if (angle > 135 && angle < 225) {
			System.out.println("abajo");
			hp -= p.dmg * 3;
		} else {
			System.out.println("costado");
			hp -= p.dmg;
		}

		hp = (hp < 0) ? 0 : hp;

	}

	private float calculateAngle(Projectile p) {
		float angle = 0;
		// get the hull center value to know the tan
		// to get the angle acord to the center
		// y sacar los radianes que tiene con respecto al centro
		float yPos = getY() + getHeight() / 2;
		float xPos = getX() + getWidth() / 2;
		float difY = p.b2body.getPosition().y;
		float difX = p.b2body.getPosition().x;
		// to understand better the math
		yPos = (yPos > difY) ? -(yPos - difY) : difY - yPos;
		xPos = (xPos > difX) ? -(xPos - difX) : difX - xPos;
		float tanValue = yPos / xPos;
		angle = (float) Math.toDegrees(Math.atan(tanValue));
		// the maths respect the sprite and hot the math.to degrees and atan works, only
		// makes between 0-90, so we had to be a bit more clever to solve that
		if (angle < 0 && (yPos < 0 && xPos > 0)) { // ANGULO ENTRO 180 Y 270 EMPEZANDO 0 ARRIBA.
			angle += 270;
		} else if (angle > 0 && (yPos < 0 && xPos < 0)) { // ANGULO ENTRO 180 Y 270 EMPEZANDO 0 ARRIBA.
			angle += 90;
		} else if (angle < 0 && yPos > 0 && xPos < 0) { // ANGULO ENTRO 180 Y 270 EMPEZANDO 0 ARRIBA.
			angle = 90 + angle;

		} else if (angle > 0 && yPos > 0 && xPos > 0) { // ANGULO ENTRO 180 Y 270 EMPEZANDO 0 ARRIBA.
			angle += 270;
		}

		angle = (rotation > angle) ? rotation - angle : angle - rotation;
		return angle;
	}

	public Hull getHull() {
		return this;
	}
}
