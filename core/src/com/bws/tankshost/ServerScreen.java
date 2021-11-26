package com.bws.tankshost;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import elements.Tank;
import input.Client;
import network.Serverside;
import tiledObjects.World2D;
import tiledObjects.WorldListener;
import utilities.Config;
import utilities.Render;
import utilities.Resources;

public class ServerScreen implements Screen {

	private SpriteBatch b;
	private OrthographicCamera camera;
	private Viewport gamePort;
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private World world;
	private Box2DDebugRenderer b2dr;
	private World2D world2d;
	private WorldListener worldListener;

	public ServerScreen() {

		///// NETWORK TEST
		Serverside server = new Serverside();

		camera = new OrthographicCamera();

		// load tiledMap
		mapLoader = new TmxMapLoader();
		map = mapLoader.load(Resources.MAP1);

		// order the render which map is going to draw
		renderer = new OrthogonalTiledMapRenderer(map, 1 / Config.PPM);

		// set map properties
		Render.world = new World(new Vector2(0, 0), true);
		world = Render.world;
		// render which draws box2d Textures
		b2dr = new Box2DDebugRenderer();
		// then camera zoom
		gamePort = new FitViewport(64 * 15 / Config.PPM, 64 * 15 / Config.PPM, camera);
		// centers the camera to the new map
		camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		// creates 2dmap per layers
		world2d = new World2D(map);

		// set the world contact listener
		worldListener = new WorldListener();
		world.setContactListener(worldListener);
		


	}

	@Override
	public void show() {

		b = Render.batch;
		gamePort.getCamera().position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

	}

	@Override
	public void render(float delta) {
		gamePort.apply();
		update(delta);
		Render.cleanScreen();
		b.setProjectionMatrix(camera.combined);
		// loads map
		renderer.render();
		// loads box2dDebugLines hitboxes
		b2dr.render(world, camera.combined);
		b.begin();
		for (int i = 0; i < Render.tanks.size(); i++) {
			Render.tanks.get(i).Render();
		}
		b.end();

	}

	private void update(float delta) {
		
		Config.delta = delta;
		camera.update();
		// 60 ticks in a second if im right
		world.step(1 / 60f, 6, 2);
		// sets whats the renderer gonna draw, that shows in camera
		renderer.setView(camera);
		
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
