package elements;

import utilities.Config;
import utilities.Render;

public abstract class Attachable extends ClientSprite implements Updateable {
	Hull hull;
	public String objectType; 
	public Attachable(String texture) {
		super(texture);
		centerImage();
		Render.addUpdateable(this);
		Render.addSprite(this);
		
	}
	
	private void centerImage() {
		
		setSize(getWidth()/2/Config.PPM,getHeight()/2/Config.PPM);
		float orgX = getWidth()/2;
		setOrigin(orgX, 0);
	}

	public void update( float x, float y,float rotation) {
		setX(x); 
		setY(y);
		setRotation(rotation);
	}
	
}
