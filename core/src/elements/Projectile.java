package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import utilities.Config;
import utilities.ClientRender;

public class Projectile extends Entity2D {
	public float speedMod;
	public Hull parent;
	public float degrees;
	public boolean fired = false;
	public boolean explode = false;
	public Texture invPix;
	float tempX;
	float tempY;
	public float dmg;
	public int id;

	public Projectile(float x, float y, Hull hull, String texture, float speedMod) {
		super(new Texture(texture),texture);
		parent = hull;
		this.world = ClientRender.world;
		this.speedMod = speedMod;
		setDegrees(hull.rotation);
		setSize(getWidth() / 2 / Config.PPM, getHeight() / 2 / Config.PPM);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setPosition(x + 0.1f, (y - getHeight() / 2) + 0.1f);
		tempY = (float) Math.cos(Math.toRadians(degrees));
		tempX = (float) Math.sin(Math.toRadians(degrees));


		createBody();
		fixtureDef();

	}

	void doMovement() {
		
		b2body.setLinearVelocity(-tempX * speedMod, tempY * speedMod);
		
		setPosition((b2body.getPosition().x  - getWidth() / 2), // if the bullet trasspass the tank hitted use +tempX/2 and -tempY/2 
				b2body.getPosition().y  - getHeight() / 2);	// these are there to "predict" the projectile and not

	}
//////////////////////


	@Override
	protected void createBody() {
		bdef = new BodyDef();
		bdef.position.set(getX() - 0.1f, getY() - 0.1f);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(getWidth() / 3);
		fdef.filter.categoryBits = Config.PROJECTIL_BIT;
		fdef.filter.maskBits = Config.TANK_BIT | Config.BARREL_BIT | Config.DEFAULT_BIT ;
		fdef.shape = shape;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);

	}



	public boolean isExploded() {
		return explode;
	}

	public void explode() {
		explode = true;
	}

	public void setDegrees(float degrees) {
		this.degrees = degrees;
		rotate(degrees);
	}

}
