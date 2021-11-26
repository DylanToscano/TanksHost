package utilities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.bws.tankshost.TanksHost;

import elements.Tank;


public abstract class Render {
	public static SpriteBatch batch;
	public static TanksHost app;
	public static World world;
	public static ArrayList<Tank> tanks = new ArrayList<Tank>();
	public static ArrayList<Sprite> renderList = new ArrayList<Sprite>();
	int renderID;

	public void render(){ //Render everything in the renderList
		for(int i=0; i<renderList.size(); i++){
			if(renderList.get(i) != null) {
				renderList.get(i).draw(batch);
			}else {
				renderList.remove(i);
			}
			
		}
	}
	public void disposeList(){
		for(int i=0; i<renderList.size(); i++){
			if(renderList.get(i) != null) {
				renderList.get(i).getTexture().dispose();;
			}else {
				renderList.remove(i);
			}
		}
	}
	
	public static void cleanScreen() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public static void addSprite(Sprite sprite) {
		renderList.add(sprite);
	}
	
}
