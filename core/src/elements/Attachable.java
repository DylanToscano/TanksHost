package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import utilities.Config;
import utilities.Render;

public abstract class Attachable extends ClientSprite implements Updateable {
	

	Hull hull;
	public String objectType; 
	
	
	public Attachable(String texture) {
		super(texture);
		centrarImagen();
		Render.addUpdateable(this);
		Render.addSprite(this);
		
	}
	
	private void centrarImagen() {
		
		setSize(getWidth()/2/Config.PPM,getHeight()/2/Config.PPM);
		float orgX = getWidth()/2;
		System.out.println(orgX);
		setOrigin(orgX, 0);
	}

	public void update( float x, float y,float rotation) {
		setX(x); // TODO: Setter and getter for this on the Resources.java
		setY(y);
		
		setRotation(rotation);
		
	}
	
	public void modifyTexture(Texture texture) {
		//this.texture.dispose(); //Gets rid of the old texture.
		setTexture(texture);
	}
}
