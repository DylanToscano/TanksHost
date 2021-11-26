package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import utilities.Config;
import utilities.Render;

public class Hull extends Entidad2D {
	public int id;
	private World world;
//	private Sprite dmged1;
//  private Sprite dmged2;
	private Sprite dmged3;

	public int startRotation;

	// stats
	private int totalHp = 0;
	private int hp = 0;
	public boolean onRoad;
	public float rotation; //degrees
	public int weaponSlots;
	public float rotationSpeed;
	public int slots;

	public Hull(String texture, int hp) {
		super(new Texture(texture));
		this.hp = hp;
		this.world = Render.world; 

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
				| Config.PROJECTIL_BIT;
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
	}

	public void disappear() {
		Render.world.destroyBody(b2body);
		b2body = null;
	}

	public void setVelocidad(float x, float y) {
		b2body.setLinearVelocity(x, y);
	}

	public void inRoad() {

		onRoad = true;
	}

	public void outRoad() {

		onRoad = false;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public boolean isOnRoad() {
		return onRoad;
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

	public void receiveDamage(Projectile p) {
		// the tank will receive the damage and the coordinates to know where it hit.
		System.out.println("NOOOO ME DA�ARON");
		System.out.println("pos x: " + p.b2body.getPosition().x);
		System.out.println("pos y: " + p.b2body.getPosition().y);
		// la camara enfoca exactamente en el centro del cubo de hitbox, lo cual facilita los calculos del angulo
//        float valorTan = (float)(entradas.getMouseY()-Config.alto/2)/((float)entradas.getMouseX()-Config.ancho/2);
//        System.out.println((entradas.getMouseY()) + " + " + (((float)entradas.getMouseX())));
//        float angulo = (float) Math.toDegrees(Math.atan(valorTan));
		float projecDegrees = (float) Math.toDegrees(Math.atan(((p.b2body.getPosition().y - getY()+getHeight()/2)/(p.b2body.getPosition().x - getX() + (getWidth()/2)))));
		System.out.println("pego a grados : " + projecDegrees);
		
		
		
	}
	public Hull getHull() {
		return this;
	}
}
