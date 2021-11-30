package tiledMapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import utilities.Config;
import utilities.ClientRender;

public abstract class InteractiveObject {
	protected boolean interaction = false;
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Shape2D limits;
	protected Body body;
	protected Fixture fixture;
	public int index;

	public InteractiveObject( TiledMap map, Rectangle limits) {
		
		this.world = ClientRender.world;
		this.limits = limits;
		this.map = map;
		// propiedades del cuerpo box2d
		FixtureDef fdef = new FixtureDef();
		// declarar el cuerpo y contiene propiedades lo necesario para crear el resto
		BodyDef bdef = new BodyDef();
		// la forma de los cuerpos
		PolygonShape shape = new PolygonShape();
		// contiene las propiedades que tendra este objeto

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((limits.getX() + limits.getWidth() / 2) / Config.PPM,
				(limits.getY() + limits.getHeight() / 2) / Config.PPM);
		// crea el cuerpo despues de determinar propiedades arriba
		shape.setAsBox(limits.getWidth() / 2 / Config.PPM, limits.getHeight() / 2 / Config.PPM);
		body = world.createBody(bdef);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
		fixture.setUserData("default");
		setFilter(Config.DEFAULT_BIT);
	}

	public void setFilter(short filterBit) {
		// se crea un filtro nuevo con el bit que le indique
		Filter filter = new Filter();
		// indica el tipo de filtro usando bitWise
		filter.categoryBits = filterBit;
		// define a la fixture de cada layerInteractivo un filtro
		fixture.setFilterData(filter);
	}
	//in case that we need to change the cell, ex: change the texture of the wall cause it gets destroyed
	public TiledMapTileLayer.Cell getCell(){
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(index);		
		return layer.getCell((int) (body.getPosition().x*Config.PPM/64),(int) (body.getPosition().y*Config.PPM/64));
		
	}
}