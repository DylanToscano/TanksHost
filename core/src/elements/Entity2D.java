package elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utilities.Render;

public abstract class Entity2D extends ClientSprite{
	// box2D
	protected FixtureDef fdef;
	protected BodyDef bdef;
	public Body b2body;
	protected World world;
	protected Fixture fixture;
	protected abstract void createBody();
	protected abstract void fixtureDef();
	
	public Entity2D(String textureRoute) {
		super(textureRoute);
	}
	public Entity2D() {
		super();
	}
	public void disappear() {
		if(b2body == null) {return;} //failsafe
		Render.world.destroyBody(b2body);
		b2body = null;
	}
}
