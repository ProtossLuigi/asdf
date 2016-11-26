package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	public static Texture img;
	public static Texture stone;
	public static Texture ground;
	public static Texture playermodel;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		stone = new Texture("stonePH.png");
		ground = new Texture("groundPH.png");
		playermodel = new Texture("playermodelPH.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(float y=0;y<Gdx.graphics.getHeight();y+=32){
			for(float x=0;x<Gdx.graphics.getWidth();x+=32){
				batch.draw(stone,x,y,32,32);
			}
		}
		batch.draw(playermodel, 32, 32, 32, 32);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		stone.dispose();
		ground.dispose();
		playermodel.dispose();
	}
}
