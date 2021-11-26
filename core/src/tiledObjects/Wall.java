package tiledObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends InteractiveObject{

	public Wall(TiledMap map, Rectangle limits) {
		super(map, limits);
		
	}

	@Override
	public void interaction() {
		System.out.println("uwu im a wall");
		
	}

}
