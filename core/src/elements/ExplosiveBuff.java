package elements;

import com.badlogic.gdx.graphics.Texture;

import utilities.Resources;

public class ExplosiveBuff extends Buff{

	public ExplosiveBuff(){
		super(Resources.ROCKETBUFF);
		b2body.setUserData("explosive");
		
	}

}
