package elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import utilities.Config;
import utilities.Functions;
import utilities.ClientRender;
import utilities.Resources;

public class CooldownBuff extends Buff{

	public CooldownBuff() {
		super(new Texture(Resources.CDBUFF),Resources.CDBUFF);
		b2body.setUserData("cooldown");
	}

	
}
