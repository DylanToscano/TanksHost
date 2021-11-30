package elements;

import com.badlogic.gdx.graphics.Texture;

import utilities.Resources;

public class SpeedBuff extends Buff {

	public SpeedBuff() {
		super(Resources.SPEEDBUFF);
		b2body.setUserData("speed");
	}

}
