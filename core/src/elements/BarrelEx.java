package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import utilities.Config;
import utilities.Functions;
import utilities.Render;
import utilities.Resources;

public class BarrelEx extends Entity2D{
	public boolean hit;
	public int id;
	public BarrelEx() {
		super(Resources.BARREL);
		this.world = Render.world;
		setSize(20/Config.PPM,20/Config.PPM);
		setPosition(Functions.randomFloat(10,50)*15/Config.PPM,Functions.randomFloat(10,50)*15/Config.PPM);
//		setPosition(3,3);
		createBody();
		fixtureDef();
	}
	
	@Override
	protected void createBody() {
		bdef = new BodyDef();
		bdef.position.set(getX()+getWidth()/2,getY()+getHeight()/2);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(getWidth()/2);
		fdef.filter.categoryBits = Config.BARREL_BIT;
		fdef.filter.maskBits = Config.EXPLOSION_BIT | Config.DEFAULT_BIT | Config.TANK_BIT | Config.PROJECTIL_BIT ;
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

	}
	public void Hitted() {
		hit = true;
		Explosion explosion = new Explosion(getX(), getY());
		Render.addSprite(explosion);
	}

}
