package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import utilities.Config;
import utilities.Functions;
import utilities.Render;
import utilities.Resources;

public class Obstacle extends Entity2D {
	private boolean vertical;
	public int id;

	public int corrections;
	public Obstacle() {
		super(Resources.BARRICADE);
		this.world = Render.world;
		setSize(getTexture().getWidth()/2/Config.PPM,getTexture().getHeight()/2/Config.PPM);
//		setPosition(Functions.randomFloat(10,50)*15/Config.PPM,Functions.randomFloat(10,50)*15/Config.PPM);
		vertical = (Functions.randomInt(1,2)==1)?false:true;
		vertical = true;
		setPosition(3,3);
		setOrigin(0,0);	
		if(vertical) {
			setRotation(90);
		}
		
//		setPosition(3.3f,3.3f);
		createBody();
		fixtureDef();
		Render.addSprite(this);
	}

	@Override
	protected void createBody() {
		bdef = new BodyDef();
		if(vertical) {
			bdef.position.set(getX()-getHeight()/2,getY()+getWidth()/2);
		}else {
			bdef.position.set(getX()+getWidth()/2,getY()+getHeight()/2);
		}
	
		bdef.type = BodyDef.BodyType.KinematicBody;
		b2body = world.createBody(bdef);

	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		// defines what kind the box is going to have
		PolygonShape shape = new PolygonShape();
		if(vertical) {
			shape.setAsBox( getHeight() / 2,getWidth() / 2);
		}else {
			shape.setAsBox(getWidth() / 2, getHeight() / 2);
		}
		fdef.filter.categoryBits = Config.DEFAULT_BIT;
		// definimos la mascara de bits, que objetos box2d tiene que darle atencion.
		fdef.filter.maskBits = Config.DEFAULT_BIT | Config.TANK_BIT 
				| Config.PROJECTIL_BIT | Config.BUFF_BIT | Config.BARREL_BIT;
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
	}
	public void correct() {
		//WIP
			if(vertical) {
				b2body.setLinearVelocity(1,0);
				setPosition((b2body.getPosition().x + getHeight()/2), // 
						b2body.getPosition().y - getWidth()/2);
			}else {
				b2body.setLinearVelocity(0,1);
				setPosition((b2body.getPosition().x - getWidth() / 2), // 
						b2body.getPosition().y - getHeight() / 2);
			}
			
		
		
	}
			
		
	
	
	public void stop() {
		b2body.setLinearVelocity(0, 0);
	}

	public void needCorrect() {
		corrections++;
		
	}
	public void fixed() {
		corrections--;
		if(corrections==0) {
			stop();
		}
	}
}
