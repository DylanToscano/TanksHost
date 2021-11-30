package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ClientSprite extends Sprite {
	//Yes. We made an entire class just to add an int, because Marconi.
	int id;
	
	public ClientSprite(Texture texture) {
		super(texture);
	}
	public ClientSprite() {
		
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public int getID() {
		return id;
	}
	
	
}
