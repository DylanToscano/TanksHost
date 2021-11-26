package tiledObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import utilities.Config;

public class Road extends InteractiveObject {
	
	public Road( TiledMap map, Rectangle limites) {
		super( map, limites);
		//crea su caja 2d
		fixture.setSensor(true);
		fixture.setUserData("Road");
		setFilter(Config.ROAD_BIT);
		
	}


	@Override
	public void interaction() {
		System.out.println("road: colision");
	}

}
