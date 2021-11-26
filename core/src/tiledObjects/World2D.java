package tiledObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;



public class World2D {

	public World2D(TiledMap mapa) {
		// abajo se crearan objetos Interactivos
		// Salidas // for each que saca cada cuadro creado por el tiledSoftware y le asigna un nuevo objeto
		//         // el numero que cambia es el numero de la capa de la que se quiere sacar los cuadros
		
//		Road rectangle
		for (MapObject object : mapa.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
		 	Rectangle rect = ((RectangleMapObject) object).getRectangle();
		 	new Road(mapa, rect);
		 }

		 
		
		//Wall
		 for (MapObject object : mapa.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
		 	//saca el rectangulo
		 	Rectangle rect = ((RectangleMapObject) object).getRectangle();
		 	// al nuevo objeto se le asigna que propiedades tendra, en este caso salida
		 	new Wall(mapa, rect);
		 }
		
	}
}
