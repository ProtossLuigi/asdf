package com.mygdx.game;

import java.util.Vector;

import java.lang.Math;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import levels.Floor;
import levels.Item;
import levels.Player;
import levels.Tile;
import levels.Direction;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	public Floor level;
	private Tile tile;
	private GameState state;
	private Textbox1 mainTextbox;
	private Textbox2 leftTextbox;
	private Textbox3 rightTextbox;
	
	private float camx=0;
	private float camy=0;
	Player player;
	boolean inputReady;
	private float playerx = 0;
	private float playery = 0;
	private int pointer = 32;
	private int[] triangle = {0,0,0,24,12,12};
	int page;
	Direction moving;
	Direction movingX;
	Direction movingY;
	
	private static float tilesize=64;
	static float playerSpeed=tilesize*4.5f;
	
	private Texture ground;
	private Texture stone;
	private Texture chest;
	private Texture playermodeldown;
	private Texture arrowRight;
	private Texture arrowLeft;
	
	BitmapFont mainFont;
	NinePatch textbox;
	ShapeRenderer triangles;
	
	@Override
	public void create () {
		
		triangle = new int[6];
		
		
		ground = new Texture("podloze2.png");
		stone = new Texture("litaskala.png");
		chest = new Texture("skrzyniazolta.png");
		playermodeldown = new Texture("playermodelPH.png");
		textbox = new NinePatch(new Texture("tbox.png"), 4, 4, 4, 4);
		arrowRight = new Texture("arrow.png");
		arrowLeft = new Texture("arrowleft.png");
		
		batch = new SpriteBatch();
		level = new Floor(0,true);
		player = new Player(4,4,Direction.RIGHT);
		state = GameState.OVERWORLD;
		
		mainFont = new BitmapFont(Gdx.files.internal("cfont.fnt"));
		mainTextbox = new Textbox1(mainFont,tilesize);
		leftTextbox = new Textbox2(mainFont,tilesize);
		rightTextbox = new Textbox3(mainFont,tilesize);
		triangles = new ShapeRenderer();
		
		inputReady = true;
		
		moving = Direction.NULL;
		movingX = Direction.NULL;
		movingY = Direction.NULL;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(inputReady){
			inputHandle();
			movePlayer();
			camUpdate();
		}
		
		
		batch.begin();
		
		for(int y=0;y<level.getheight();y++){
			for(int x=0;x<level.getwidth();x++){
				batch.draw(findTexture(level.getTile(x,y)), x*tilesize-camx, y*tilesize-camy, tilesize, tilesize);
			}
		}
		
		batch.draw(findPlayermodel(Direction.RIGHT), player.coordx*tilesize+playerx-camx, player.coordy*tilesize+playery-camy, tilesize, tilesize);
		
		drawTextBox();
		
		batch.end();
		//drawShapes();
	}
	
	private void drawTextBox(){
		switch(state){
		case OVERWORLD:
			if(mainTextbox.getReady() || leftTextbox.getReady() || rightTextbox.getReady()){
				if(mainTextbox.getReady()){
					mainTextbox.close();
					if(mainTextbox.x != 0)
						textbox.draw(batch, mainTextbox.x, mainTextbox.y, mainTextbox.getWidth(), mainTextbox.getHeight());
				}
				if(leftTextbox.getReady()){
					leftTextbox.close();
					if(leftTextbox.x != 0)
						textbox.draw(batch, leftTextbox.x, leftTextbox.y, leftTextbox.getWidth(), leftTextbox.getHeight());
				}
				if(rightTextbox.getReady()){
					rightTextbox.close();
					if(rightTextbox.x != 0)
						textbox.draw(batch, rightTextbox.x, rightTextbox.y, rightTextbox.getWidth(), rightTextbox.getHeight());
				}
			}
			else
				inputReady = true;
			break;
		case TILEINTERACTION:
			if(mainTextbox.getReady()){
				inputReady = true;
				textbox.draw(batch, mainTextbox.x, mainTextbox.y, mainTextbox.getWidth(), mainTextbox.getHeight());
				switch(tile.getType()){
				case chest:
					if(tile.getItems().isEmpty()){
						pointer = 32;
						mainFont.draw(batch, "The chest is empty.", mainTextbox.getTextX(0), mainTextbox.getTextY(0));
					}
					else{
						if(pointer == 32){
							pointer = 0;
						}
						mainFont.draw(batch, "You find items in the chest!", mainTextbox.getTextX(0), mainTextbox.getTextY(0));
						Vector<String> items = new Vector<String>();
						System.out.println(tile.getItems().size());
						for(int i=0;i<tile.getItems().size();i++){
							System.out.println(tile.getItems().get(i).getName());
							items.addElement(tile.getItems().get(i).getName());
						}
						page = (int)Math.floor((double)pointer/6);
						for(int i=page*6;i<items.size() && i<page*6+3;i+=1){
							System.out.println(pointer);
							mainFont.draw(batch, items.get(i), mainTextbox.getTextX(i-page*6+1), mainTextbox.getTextY(2));
						}
						for(int i=page*6+3;i<items.size() && i<page*6+6;i++){
							mainFont.draw(batch, items.get(i), mainTextbox.getTextX(i-page*6-2), mainTextbox.getTextY(1));
						}
						triangles.begin(ShapeRenderer.ShapeType.Filled);
						triangles.setColor(255, 255, 255, 255);
						if(page > 0){
							batch.draw(arrowLeft, mainTextbox.x+mainTextbox.getBorder(), mainTextbox.y+mainTextbox.getBorder(), 16, 16);
						}
						if(page*6+6<tile.getItems().size()){
							batch.draw(arrowRight, mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-16, mainTextbox.y+mainTextbox.getBorder(), 16, 16);
						}
						float tx = mainTextbox.getTextX(Math.floorMod(pointer, 3)+1)-20;
						float ty = mainTextbox.getTextY(2-Math.floorMod(Math.floorDiv(pointer, 3),2))-18;
						batch.draw(arrowRight, tx, ty, 16, 16);
						triangles.end();
					}
					break;
				default:
					throw new IllegalStateException();
				}
			}
			else{
				mainTextbox.open(state);
				textbox.draw(batch, mainTextbox.x, mainTextbox.y, mainTextbox.getWidth(), mainTextbox.getHeight());
			}
			break;
		case COMBAT:
			if(mainTextbox.getReady() || leftTextbox.getReady() || rightTextbox.getReady()){
				
			}
			else{
				if(mainTextbox.getReady() == false){
					mainTextbox.open(GameState.COMBAT);
				}
				if(leftTextbox.getReady() == false){
					leftTextbox.open(GameState.COMBAT);
				}
				if(rightTextbox.getReady() == false){
					rightTextbox.open(GameState.COMBAT);
				}
			}
			break;
		default:
			throw new IllegalStateException();
		}
	}
	
	private void drawShapes(){
		switch(state){
		case TILEINTERACTION:
			switch(tile.getType()){
			case chest:
				triangles.begin(ShapeRenderer.ShapeType.Filled);
				triangles.setColor(1, 1, 1, 1);
				if(page > 0){
					triangles.triangle(mainTextbox.x+mainTextbox.getBorder()-triangle[0], mainTextbox.y+mainTextbox.getBorder()+triangle[1], mainTextbox.x+mainTextbox.getBorder()-triangle[2], mainTextbox.y+mainTextbox.getBorder()+triangle[3], mainTextbox.x+mainTextbox.getBorder()-triangle[4], mainTextbox.y+mainTextbox.getBorder()+triangle[5]);
				}
				if(page*6+6<tile.getItems().size()){
					triangles.triangle(mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-12+triangle[0], mainTextbox.y+mainTextbox.getBorder()+triangle[1], mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-12+triangle[2], mainTextbox.y+mainTextbox.getBorder()+triangle[3], mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-12+triangle[4], mainTextbox.y+mainTextbox.getBorder()+triangle[5]);
				}
				float tx = mainTextbox.getTextX(Math.floorMod(pointer, 3)+1)-16;
				float ty = mainTextbox.getTextY(2-Math.floorMod(Math.floorDiv(pointer, 3),2));
				triangles.triangle(tx+triangle[0], ty+triangle[1], tx+triangle[2], ty+triangle[3], tx+triangle[4], ty+triangle[5]);
				triangles.end();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private void inputHandle(){
		switch(state){
		case OVERWORLD:
			if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
				if(interact())
					return;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP) && ! Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				movingY = Direction.UP;
				player.facing = Direction.UP;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && ! Gdx.input.isKeyPressed(Input.Keys.UP)){
				movingY = Direction.DOWN;
				player.facing = Direction.DOWN;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && ! Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				movingX = Direction.LEFT;
				player.facing = Direction.LEFT;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && ! Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				movingX = Direction.RIGHT;
				player.facing = Direction.RIGHT;
			}
			break;
		case TILEINTERACTION:
			switch(tile.getType()){
			case chest:
				if(tile.getItems().isEmpty() && Gdx.input.isKeyJustPressed(Input.Keys.Z)){
					pointer = 32;
					state = GameState.OVERWORLD;
					inputReady = false;
				}
				if(tile.getItems().isEmpty() == false){
					if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
						player.loot(tile.getItems().get(pointer));
						tile.getItems().remove(pointer);
						if(tile.getItems().size() == 0){
							pointer = 32;
							state = GameState.OVERWORLD;
							inputReady = false;
							return;
						}
						if(pointer >= tile.getItems().size()){
							pointer--;
						}
						return;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
						pointer = 32;
						state = GameState.OVERWORLD;
						inputReady = false;
						return;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
						if(Math.floorMod(pointer, 3) == 2 && pointer+4<tile.getItems().size()){
							pointer+=4;
						}
						else if(pointer+1<tile.getItems().size()){
							pointer++;
						}
						return;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
						if(Math.floorMod(pointer, 3) == 0 && pointer>3){
							pointer-=4;
						}
						else if(pointer != 0 && pointer != 3)
							pointer--;
						return;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
						if(Math.floorMod(pointer, 6) > 2){
							pointer-=3;
						}
						return;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
						if(Math.floorMod(pointer, 6) < 3 && pointer+3<tile.getItems().size()){
							pointer+=3;
						}
						return;
					}
				}
			default:
				break;
			}
			break;
		default:
			throw new IllegalStateException();
		}
		
	}
	
	private void movePlayer(){
		if(canMove(movingX) == false)
			movingX = Direction.NULL;
		if(canMove(movingY) == false)
			movingY = Direction.NULL;
		switch(movingX){
		case LEFT:
			switch(movingY){
			case UP:
				playerx-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
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
				playerx+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				playery+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			case DOWN:
				playerx+=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				playery-=playerSpeed*Gdx.graphics.getDeltaTime()*0.7071f;
				break;
			default:
				playerx+=playerSpeed*Gdx.graphics.getDeltaTime();
				break;
			}
			break;
		default:
			switch(movingY){
			case UP:
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
			if(level.getTile(player.coordx-1, player.coordy).getWalkable() && (playery == 0 || level.getTile(player.coordx-1, player.coordy+1).getWalkable())){
				player.coordx--;
				playerx+=tilesize;
			}
			else{
				playerx=0;
			}
		}
		if(playerx >= tilesize){
			player.coordx++;
			if(level.getTile(player.coordx+1, player.coordy).getWalkable() && (playerx == 0 || level.getTile(player.coordx+1, player.coordy+1).getWalkable())){
				playerx-=tilesize;
			}
			else{
				playerx = 0;
			}
		}
		if(playery < 0){
			if(level.getTile(player.coordx, player.coordy-1).getWalkable() && (playerx == 0 || level.getTile(player.coordx+1, player.coordy-1).getWalkable())){
				player.coordy--;
				playery+=tilesize;
			}
			else{
				playery=0;
			}
		}
		if(playery >= tilesize){
			player.coordy++;
			if(level.getTile(player.coordx, player.coordy+1).getWalkable() && (playerx == 0 || level.getTile(player.coordx+1, player.coordy+1).getWalkable())){
				playery-=tilesize;
			}
			else{
				playery = 0;
			}
		}
		movingX = Direction.NULL;
		movingY = Direction.NULL;
	}
	
	private boolean canMove(Direction way){
		switch(way){
		case UP:
			if(playery == 0){
				if(playerx > 0)
					return (level.getTile(player.coordx, player.coordy+1).getWalkable() && level.getTile(player.coordx+1, player.coordy+1).getWalkable());
				else
					return level.getTile(player.coordx, player.coordy+1).getWalkable();
			}
			else
				return true;
		case DOWN:
			if(playery == 0){
				if(playerx > 0)
					return (level.getTile(player.coordx, player.coordy-1).getWalkable() && level.getTile(player.coordx+1, player.coordy-1).getWalkable());
				else
					return level.getTile(player.coordx, player.coordy-1).getWalkable();
			}
			else
				return true;
		case LEFT:
			if(playerx == 0){
				if(playery > 0)
					return level.getTile(player.coordx-1, player.coordy).getWalkable() && level.getTile(player.coordx-1, player.coordy+1).getWalkable();
				else
					return level.getTile(player.coordx-1, player.coordy).getWalkable();
			}
			else
				return true;
		case RIGHT:
			if(playerx == 0){
				if(playery > 0)
					return level.getTile(player.coordx+1, player.coordy).getWalkable() && level.getTile(player.coordx+1, player.coordy+1).getWalkable();
				else
					return level.getTile(player.coordx+1, player.coordy).getWalkable();
			}
			else
				return true;
		default:
			return true;
		}
	}
	
	private boolean interact(){
		switch(player.facing){
		case UP:
			if(playerx>tilesize/2)
				tile = level.getTile(player.coordx+1,player.coordy+1);
			else
				tile = level.getTile(player.coordx,player.coordy+1);
			break;
		case DOWN:
			if(playery>0)
				return false;
			if(playerx>tilesize/2)
				tile = level.getTile(player.coordx+1,player.coordy-1);
			else
				tile = level.getTile(player.coordx,player.coordy-1);
			break;
		case RIGHT:
			if(playery>tilesize/2)
				tile = level.getTile(player.coordx+1,player.coordy+1);
			else
				tile = level.getTile(player.coordx+1,player.coordy);
			break;
		case LEFT:
			if(playerx>0)
				return false;
			if(playery>tilesize/2)
				tile = level.getTile(player.coordx-1,player.coordy+1);
			else
				tile = level.getTile(player.coordx-1,player.coordy);
			break;
		default:
			throw new IllegalStateException();
		}
		if(tile.getInteractible()){
			state = GameState.TILEINTERACTION;
			inputReady = false;
			return true;
		}
		else{
			return false;
		}
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
		switch (tile.getType()) {
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
			return playermodeldown;
		}
	}
	
	@Override
	public void dispose () {
		ground.dispose();
		stone.dispose();
		chest.dispose();
		batch.dispose();
		playermodeldown.dispose();
		mainFont.dispose();
		arrowRight.dispose();
	}
}
