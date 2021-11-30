package tiledMapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import utilities.Config;

public class Wall extends InteractiveObject{

	public Wall(TiledMap map, Rectangle limits) {
		super(map, limits);
		fixture.setUserData("obstacle");
		setFilter(Config.DEFAULT_BIT);
	}

}
