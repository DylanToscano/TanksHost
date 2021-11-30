package com.bws.tankshost;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import network.Serverside;
import network.ServersideThread;
import utilities.ClientRender;

public class TanksHost extends Game {
	
	public void create () {
		ClientRender.app = this;
		ClientRender.batch = new SpriteBatch();
		this.setScreen(new ServerScreen());
	}

	public void render () {
		super.render();
	}
	
	public void dispose () {
		ServersideThread sThread = Serverside.getHs();
		if(sThread != null) {sThread.stopServer();}
		ClientRender.batch.dispose();
	}
}
