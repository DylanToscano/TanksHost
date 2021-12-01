package com.bws.tankshost;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import elements.BarrelEx;
import elements.Buff;
import elements.CooldownBuff;
import elements.ExplosiveBuff;
import elements.Obstacle;
import elements.SpeedBuff;
import network.Serverside;
import tiledMapObjects.World2D;
import tiledMapObjects.WorldListener;
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
	private boolean buffs = false;

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
		Render.render();
		Render.updateList();
		
	}

	private void update(float delta) {

		Config.delta = delta;
		camera.update();
		// 60 ticks in a second if im right
		world.step(1 / 60f, 6, 2);
		// sets whats the renderer gonna draw, that shows in camera
		renderer.setView(camera); 
		
		crearBuffs();
	}

//	private void correctObstacle() {  WIP TO NO TOUCH
//
//		if (obstacle1.corrections > 0 && time < 0.2f) {
//			obstacle1.correct();
//		} else {
//			obstacle1.fixed();
//
//			if (obstacle1.corrections > 0) {
//				time = 0;
//			}
//		}
//
//	}
	
	public void crearBuffs() {
		
		if(!buffs && Render.getServerThread()!= null) {
			System.out.println(Render.getServerThread());
			Buff buff1 = new CooldownBuff(); 
			Buff buff2 = new ExplosiveBuff(); 
			Buff buff3 = new SpeedBuff(); 
			Obstacle obstacle1 = new Obstacle();
			Obstacle obstacle2 = new Obstacle();
			Obstacle obstacle3 = new Obstacle();
			BarrelEx barrel1 = new BarrelEx();
			BarrelEx barrel2 = new BarrelEx();
			BarrelEx barrel3 = new BarrelEx();
			Render.addSprite(barrel1);
			Render.addSprite(barrel2);
			Render.addSprite(barrel3);
			Render.addSprite(buff1);
			Render.addSprite(buff2);
			Render.addSprite(buff3);
			Render.addSprite(obstacle1);
			Render.addSprite(obstacle2);
			Render.addSprite(obstacle3);
			buffs = true;
		}
		
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
