package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utilities.ClientRender;

public abstract class Entity2D extends ClientSprite{
	// box2D
	protected FixtureDef fdef;
	protected BodyDef bdef;
	public Body b2body;
	protected World world;
	protected Fixture fixture;
	protected abstract void createBody();
	protected abstract void fixtureDef();
	
	public Entity2D(Texture texture, String textureRoute) {
		super(texture,textureRoute);
	}
	public Entity2D() {
		super();
	}
	public void disappear() {
		ClientRender.world.destroyBody(b2body);
		b2body = null;
	}
}
