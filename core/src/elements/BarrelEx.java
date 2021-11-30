package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import utilities.Config;
import utilities.Functions;
import utilities.Render;
import utilities.Resources;

public class BarrelEx extends Entidad2D{
	public boolean hitted;
	public BarrelEx() {
		super(new Texture(Resources.BARREL));
		this.world = Render.world;
		setSize(20/Config.PPM,20/Config.PPM);
		setPosition(Functions.randomFloat(10,50)*15/Config.PPM,Functions.randomFloat(10,50)*15/Config.PPM);
		createBody();
		fixtureDef();
	}
	
	@Override
	protected void createBody() {
		bdef = new BodyDef();
		bdef.position.set(getX()+getWidth()/2,getY()+getHeight()/2);
		bdef.type = BodyDef.BodyType.KinematicBody;
		b2body = world.createBody(bdef);
		
		
	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(getWidth()/2);
		fdef.filter.categoryBits = Config.BARREL_BIT;
		fdef.filter.maskBits = Config.TANK_BIT | Config.PROJECTIL_BIT | Config.EXPLOSION_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);

	}
	public void Hitted() {
		hitted = true;
		Explosion explosion = new Explosion(getX(), getY());
		Render.addSprite(explosion);
	}

}
