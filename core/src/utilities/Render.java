package utilities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.bws.tankshost.TanksHost;

import elements.BarrelEx;
import elements.Buff;
import elements.ClientSprite;
import elements.Explosion;
import elements.Projectile;
import elements.Tank;
import elements.Updateable;
import network.ServerClient;
import network.ServersideThread;

public abstract class Render {
	public static SpriteBatch batch;
	public static TanksHost app;
	public static World world;
	public static ArrayList<Tank> tanks = new ArrayList<Tank>();
	public static ArrayList<ClientSprite> renderList = new ArrayList<ClientSprite>();
	public static ArrayList<Updateable> updateList = new ArrayList<Updateable>();
	static ServersideThread serversideThread;

	public static void render() { // Render everything in the renderList
		batch.begin();
		for (int i = 0; i < renderList.size(); i++) {
			if (renderList.get(i) != null) {
				renderList.get(i).draw(batch);
				try {
					// before it takes that the Sprite is intance of The class we want to ask, so it
					// doesnt cast a buff as a projectile.
					// here it takes if a projectile is exploded or a buff picked so it disappear,
					// cause
					// we cant disappear it at the moment of the world listener cause the game
					// crashes because the world2d is locked.

					if (renderList.get(i) instanceof Buff && ((Buff) renderList.get(i)).isPicked()) {

						((Buff) renderList.get(i)).disappear();
						;
						renderList.remove(i);

					} else if (renderList.get(i) instanceof Projectile
							&& ((Projectile) renderList.get(i)).isExploded()) {
						((Projectile) renderList.get(i)).disappear();

						renderList.remove(i);
					} else if (renderList.get(i) instanceof Explosion && ((Explosion) renderList.get(i)).end) {
						renderList.remove(i);
					} else if (renderList.get(i) instanceof BarrelEx && ((BarrelEx) renderList.get(i)).hit) {
						((BarrelEx) renderList.get(i)).disappear();
						renderList.remove(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				renderList.remove(i);
			}

		}
		batch.end();
	}

	public static void disposeList() {
		for (int i = 0; i < renderList.size(); i++) {
			if (renderList.get(i) != null) {
				renderList.get(i).getTexture().dispose();
				;
			} else {
				renderList.remove(i);
			}
		}
	}

	public static void updateList() {
		for (int i = 0; i < updateList.size(); i++) {
			if (updateList.get(i) != null) {

				try {
					updateList.get(i).update();
					if (((Explosion) updateList.get(i)).end) {
						updateList.remove(i);
					}
				} catch (Exception e) {

				}

			} else {
				updateList.remove(i);
			}
		}
	}

	public static void cleanScreen() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public static void setServerThread(ServersideThread thread) {
		serversideThread = thread;
	}
	
	public static ServersideThread getServerThread() {
		return serversideThread;
	}

	public static void addUpdateable(Updateable update) {
		updateList.add(update);
	}

	public static void addSprite(ClientSprite sprite) {
		sprite.setID(serversideThread.generateSpriteID());
		renderList.add(sprite);
		serversideThread.addSprite(sprite);
	}

	public static void removeSprite() {

	}
	
//////TANK MANAGEMENT
	public static void createTank(ServerClient client) {
		tanks.add(new Tank(client));
	}
	
	public static void removeTank(ServerClient client) {
		Tank removedTank = null;
		for (int i = 0; i < tanks.size(); i++) {
			if(tanks.get(i).owner == client) {
				removedTank = tanks.get(i);
				break;
			}
		}
		if (removedTank != null){
			
		}
	}

}
