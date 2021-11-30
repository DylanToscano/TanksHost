package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import utilities.Config;
import utilities.Functions;
import utilities.Render;

public abstract class Buff extends Entity2D {
	private boolean picked;
	public int id;
	public Buff(String textureRoute) {
		super(textureRoute);
		//random position between the center map
		setSize(40/Config.PPM,40/Config.PPM);
		setPosition(Functions.randomFloat(10,50)*15/Config.PPM,Functions.randomFloat(10,50)*15/Config.PPM);
		
//		setPosition(3F+Config.counter/2,3f);
//		Config.counter++;
		this.world = Render.world;
		createBody();
		fixtureDef();
		
	}

	@Override
	protected void createBody() {
		bdef = new BodyDef();
		
		
		bdef.position.set(getX()+getWidth()/2, getY()+getHeight()/2);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		// defines what kind the box is going to have
		CircleShape shape = new CircleShape();

		shape.setRadius(getWidth() / 2 );
		fdef.filter.categoryBits = Config.BUFF_BIT;
		// definimos la mascara de bits, que objetos box2d tiene que darle atencion.
		fdef.filter.maskBits =  Config.TANK_BIT | Config.DEFAULT_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
	}
	public  void pick(){
		System.out.println("se agarro el buff uwu");
		picked = true;
	}

	public boolean isPicked() {
		
		return picked;
	}

}
