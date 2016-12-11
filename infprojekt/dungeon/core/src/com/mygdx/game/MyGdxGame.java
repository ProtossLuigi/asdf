package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import levels.Floor;
import levels.Player;
import levels.Tile;
import levels.Direction;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	public Floor level;
	static float tilesize=64;
	float camx=0;
	float camy=0;
	Player player;
	boolean inputReady;
	float playerx;
	float playery;
	Direction moving;
	static float playerSpeed=tilesize*4f;
	
	private Texture ground;
	private Texture stone;
	
	@Override
	public void create () {
		
		ground = new Texture("groundPH.png");
		stone = new Texture("stonePH.png");
		
		batch = new SpriteBatch();
		level = new Floor(0,true);
		player = new Player(4,4,Direction.RIGHT);
		playerx = player.coordx*tilesize;
		playery = player.coordy*tilesize;
		inputReady = true;
		moving = Direction.NULL;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(inputReady==true){
			inputHandle();
		}
		movePlayer();
		camUpdate();
		
		batch.begin();
		
		/*for(float y=0;y<Gdx.graphics.getHeight();y+=32){
			for(float x=0;x<Gdx.graphics.getWidth();x+=32){
				batch.draw(stone,x,y,32,32);
			}
		}
		batch.draw(playermodel, 32, 32, 32, 32);*/
		
		for(int y=0;y<level.getheight();y++){
			for(int x=0;x<level.getwidth();x++){
				batch.draw(findTexture(level.getTile(x,y)), x*tilesize-camx, y*tilesize-camy, tilesize, tilesize);
			}
		}
		
		batch.draw(findPlayermodel(Direction.RIGHT), playerx-camx, playery-camy, tilesize, tilesize);
		
		batch.end();
	}
	
	private void inputHandle(){
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(player.move(Direction.UP,level)==true){
				moving = Direction.UP;
				inputReady = false;
			}
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(player.move(Direction.DOWN,level)==true){
				moving = Direction.DOWN;
				inputReady = false;
			}
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(player.move(Direction.LEFT,level)==true){
				moving = Direction.LEFT;
				inputReady = false;
			}
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(player.move(Direction.RIGHT,level)==true){
				moving = Direction.RIGHT;
				inputReady = false;
			}
			return;
		}
	}
	
	private void movePlayer(){
		switch(moving){
		case UP:
			playery+=playerSpeed*Gdx.graphics.getDeltaTime();
			if(playery >= player.coordy*tilesize){
				playery = player.coordy*tilesize;
				moving = Direction.NULL;
				inputReady = true;
			}
			break;
		case DOWN:
			playery-=playerSpeed*Gdx.graphics.getDeltaTime();
			if(playery <= player.coordy*tilesize){
				playery = player.coordy*tilesize;
				moving = Direction.NULL;
				inputReady = true;
			}
			break;
		case LEFT:
			playerx-=playerSpeed*Gdx.graphics.getDeltaTime();
			if(playerx <= player.coordx*tilesize){
				playerx = player.coordx*tilesize;
				moving = Direction.NULL;
				inputReady = true;
			}
			break;
		case RIGHT:
			playerx+=playerSpeed*Gdx.graphics.getDeltaTime();
			if(playerx >= player.coordx*tilesize){
				playerx = player.coordx*tilesize;
				moving = Direction.NULL;
				inputReady = true;
			}
			break;
		case NULL:
			return;
		}
	}
	
	private void camUpdate(){
		camx = playerx-(Gdx.graphics.getWidth()-tilesize)/2;
		if(camx<0)
			camx = 0;
		if(camx>level.getwidth()*tilesize-Gdx.graphics.getWidth())
			camx = level.getwidth()*tilesize-Gdx.graphics.getWidth();
		camy = playery-(Gdx.graphics.getHeight()-tilesize)/2;
		if(camy<0)
			camy = 0;
		if(camy > level.getheight()*tilesize-Gdx.graphics.getHeight())
			camy = level.getheight()*tilesize-Gdx.graphics.getHeight();
	}
	
	private Texture findTexture(Tile tile) {
		switch (tile.gettype()) {
		case ground:
			return ground;
		case stone:
			return stone;
		default:
			throw new IllegalStateException();
		}
	}
	
	private Texture findPlayermodel(Direction facing){
		switch(facing){
		default:
			return new Texture("playermodelPH.png");
		}
	}
	
	@Override
	public void dispose () {
		ground.dispose();
		stone.dispose();
		batch.dispose();
	}
}
