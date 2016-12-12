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
	private static float tilesize=64;
	private float camx=0;
	private float camy=0;
	Player player;
	boolean inputReady;
	float playerx = 0;
	float playery = 0;
	Direction moving;
	Direction movingX;
	Direction movingY;
	static float playerSpeed=tilesize*4.5f;
	
	private Texture ground;
	private Texture stone;
	private Texture chest;
	
	@Override
	public void create () {
		
		ground = new Texture("podloze2.png");
		stone = new Texture("litaskala.png");
		chest = new Texture("skrzyniazolta.png");
		
		batch = new SpriteBatch();
		level = new Floor(0,true);
		player = new Player(4,4,Direction.RIGHT);
		
		inputReady = true;
		moving = Direction.NULL;
		movingX = Direction.NULL;
		movingY = Direction.NULL;
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
		
		for(int y=0;y<level.getheight();y++){
			for(int x=0;x<level.getwidth();x++){
				batch.draw(findTexture(level.getTile(x,y)), x*tilesize-camx, y*tilesize-camy, tilesize, tilesize);
			}
		}
		
		batch.draw(findPlayermodel(Direction.RIGHT), player.coordx*tilesize+playerx-camx, player.coordy*tilesize+playery-camy, tilesize, tilesize);
		
		batch.end();
	}
	
	private void inputHandle(){
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && ! Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			movingY = Direction.UP;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && ! Gdx.input.isKeyPressed(Input.Keys.UP)){
			movingY = Direction.DOWN;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && ! Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			movingX = Direction.LEFT;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && ! Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			movingX = Direction.RIGHT;
		}
	}
	
	private void movePlayer(){
		switch(movingX){
		case LEFT:
			switch(movingY){
			case UP:
				playerx-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				if(playery != 0 || level.getTile(player.coordx, player.coordy+1).getWalkable())
					playery+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			case DOWN:
				playerx-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				playery-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			default:
				playerx-=playerSpeed*Gdx.graphics.getDeltaTime();
				break;
			}
			break;
		case RIGHT:
			switch(movingY){
			case UP:
				if(playerx != 0 || level.getTile(player.coordx+1, player.coordy).getWalkable())
					playerx+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				if(playery != 0 || level.getTile(player.coordx, player.coordy+1).getWalkable())
					playery+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			case DOWN:
				if(playerx != 0 || level.getTile(player.coordx+1, player.coordy).getWalkable())
					playerx+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				playery-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			default:
				if(playerx != 0 || level.getTile(player.coordx+1, player.coordy).getWalkable())
					playerx+=playerSpeed*Gdx.graphics.getDeltaTime();
				break;
			}
			break;
		default:
			switch(movingY){
			case UP:
				if(playery != 0 || level.getTile(player.coordx, player.coordy+1).getWalkable())
					playery+=playerSpeed*Gdx.graphics.getDeltaTime();
				break;
			case DOWN:
				playery-=playerSpeed*Gdx.graphics.getDeltaTime();
				break;
			default:
				break;
			}
			break;
		}
		
		if(playerx < 0){
			if(level.getTile(player.coordx-1, player.coordy).getWalkable()){
				player.coordx--;
				playerx+=tilesize;
			}
			else{
				playerx=0;
			}
		}
		if(playerx >= tilesize){
			player.coordx++;
			if(level.getTile(player.coordx+1, player.coordy).getWalkable()){
				playerx-=tilesize;
			}
			else{
				playerx = 0;
			}
		}
		if(playery < 0){
			if(level.getTile(player.coordx, player.coordy-1).getWalkable()){
				player.coordy--;
				playery+=tilesize;
			}
			else{
				playery=0;
			}
		}
		if(playery >= tilesize){
			player.coordy++;
			if(level.getTile(player.coordx, player.coordy+1).getWalkable()){
				playery-=tilesize;
			}
			else{
				playery = 0;
			}
		}
		movingX = Direction.NULL;
		movingY = Direction.NULL;
	}
	
	private void camUpdate(){
		camx = player.coordx*tilesize+playerx-(Gdx.graphics.getWidth()-tilesize)/2;
		if(camx<0)
			camx = 0;
		if(camx>level.getwidth()*tilesize-Gdx.graphics.getWidth())
			camx = level.getwidth()*tilesize-Gdx.graphics.getWidth();
		camy = player.coordy*tilesize+playery-(Gdx.graphics.getHeight()-tilesize)/2;
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
		case chest:
			return chest;
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
		chest.dispose();
		batch.dispose();
	}
}
