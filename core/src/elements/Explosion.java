package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import utilities.Config;
import utilities.ClientRender;
import utilities.Resources;

public class Explosion extends Entity2D implements Updateable{
	//this is gonna be an animation, so we had to start with that.
	private float radAux;
	private Animation<Sprite> animation;
	private Array<Sprite> frames;
	private float counter;
	public boolean end;
	private float x,y;
	private float dmg = 150;//see if we can change the value accord the barrel or bullet
	//in this case the frameSize will always be 5,(cause we only have 5 sprites xd)
	private float framesSize = 5;
	public Explosion(float x,float y) { 
		world = ClientRender.world;
		
		setPosition(x,y);
		setAnimation();
		setRegion(new Texture(Resources.EXPLOSION+1+".png"));
		this.x = x;
		this.y = y;
		ClientRender.addUpdateable(this);
		ClientRender.addSprite(this);
		counter = 0;
	}
	
	public void update() {
		counter += Config.delta;
		if(counter < (framesSize/10)) {
			radAux += Config.delta;
			if(b2body != null) {
				disappear();
			}
			setRegion(getFrame());
			//we define the explosion here cause the sensor would expand			
			createBody();
			fixtureDef();
			setSize(getRegionWidth()/Config.PPM,getRegionHeight()/Config.PPM);
			setPosition(x-getWidth()/2,y-getHeight()/2);
		}else {
			disappear();
			end = true;
		}
	}


	@Override
	protected void createBody() {
		bdef = new BodyDef();
		bdef.position.set(x,y);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		
	}

	@Override
	protected void fixtureDef() {
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(radAux);
		fdef.filter.categoryBits = Config.EXPLOSION_BIT;
		fdef.filter.maskBits = Config.TANK_BIT | Config.BARREL_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);

	}
	private Sprite getFrame() {
		return animation.getKeyFrame(counter);
	}
	private void setAnimation() {
		frames = new Array<Sprite>();
		for (int i = 0; i < framesSize; i++) {
			frames.add(new Sprite(new Texture(Resources.EXPLOSION+ (i+1)+".png")));
		}
		animation = new Animation<Sprite>(0.1f,frames);
	}
	public float getDmg() {
		return dmg;
	}



}
