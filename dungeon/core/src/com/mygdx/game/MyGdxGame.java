package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
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
import levels.Rat;
import levels.Tile;
import levels.Ability;
import levels.AbilityEffect;
import levels.Attack;
import levels.Consumable;
import levels.Direction;
import levels.Enemy;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	public Floor level;
	private Tile tile;
	private GameState state;
	private CombatState combatstate;
	private Textbox1 mainTextbox;
	private Textbox2 leftTextbox;
	private Textbox3 rightTextbox;
	
	private float camx=0;
	private float camy=0;
	Player player;
	Enemy enemy;
	boolean inputReady;
	boolean enemyApproaching;
	private float playerx = 0;
	private float playery = 0;
	private int pointer = 32;
	int page;
	Direction moving;
	Direction movingX;
	Direction movingY;
	Combat combat;
	private List<AbilityEffect> activeEffects;
	private Ability activeAbility;
	
	private boolean activetbox;
	private List<String> options;
	private String header;
	private int menu;
	// 0 main menu, 1 abilities, 2 items
	private int abilitySlide;
	private boolean canUseItems;
	private List<Consumable> playerItems;
	private List<Ability> availableAbilities;
	
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
	
	@Override
	public void create () {
		
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
		
		inputReady = true;
		enemyApproaching = false;
		
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
		
		if(enemyApproaching){
			initCombat();
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
					mainFont.draw(batch, header, mainTextbox.getTextX(0), mainTextbox.getTextY(0));
					if(activetbox){
						page = (int)Math.floor((double)pointer/6);
						for(int i=page*6;i<options.size() && i<page*6+3;i+=1){
							//System.out.println(pointer);
							mainFont.draw(batch, options.get(i), mainTextbox.getTextX(i-page*6+1), mainTextbox.getTextY(2));
						}
						for(int i=page*6+3;i<options.size() && i<page*6+6;i++){
							mainFont.draw(batch, options.get(i), mainTextbox.getTextX(i-page*6-2), mainTextbox.getTextY(1));
						}
						if(page > 0){
							batch.draw(arrowLeft, mainTextbox.x+mainTextbox.getBorder(), mainTextbox.y+mainTextbox.getBorder(), 16, 16);
						}
						if(page*6+6<options.size()){
							batch.draw(arrowRight, mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-16, mainTextbox.y+mainTextbox.getBorder(), 16, 16);
						}
						float tx = mainTextbox.getTextX(Math.floorMod(pointer, 3)+1)-20;
						float ty = mainTextbox.getTextY(2-Math.floorMod(Math.floorDiv(pointer, 3),2))-18;
						batch.draw(arrowRight, tx, ty, 16, 16);
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
				inputReady = true;
				textbox.draw(batch, mainTextbox.x, mainTextbox.y, mainTextbox.getWidth(), mainTextbox.getHeight());
				textbox.draw(batch, leftTextbox.x, leftTextbox.y, leftTextbox.getWidth(), leftTextbox.getHeight());
				textbox.draw(batch, rightTextbox.x, rightTextbox.y, rightTextbox.getWidth(), rightTextbox.getHeight());
				mainFont.draw(batch, "HP", leftTextbox.getTextX(), leftTextbox.getTextY(0));
				mainFont.draw(batch, player.getHP() + " / " + player.getMaxHP(), leftTextbox.getTextX(), leftTextbox.getTextY(1));
				mainFont.draw(batch, "MANA", leftTextbox.getTextX(), leftTextbox.getTextY(2));
				mainFont.draw(batch, player.getMana() + " / " + player.getMaxMana(), leftTextbox.getTextX(), leftTextbox.getTextY(3));
				mainFont.draw(batch, "HP", rightTextbox.getTextX(), rightTextbox.getTextY(0));
				mainFont.draw(batch, enemy.getHP() + " / " + enemy.getMaxHP(), rightTextbox.getTextX(), rightTextbox.getTextY(1));
				mainFont.draw(batch, header, mainTextbox.getTextX(0), mainTextbox.getTextY(0));
				if(activetbox){
					page = (int)Math.floor((double)pointer/6);
					for(int i=page*6;i<options.size() && i<page*6+3;i+=1){
						//System.out.println(pointer);
						mainFont.draw(batch, options.get(i), mainTextbox.getTextX(i-page*6+1), mainTextbox.getTextY(2));
					}
					for(int i=page*6+3;i<options.size() && i<page*6+6;i++){
						mainFont.draw(batch, options.get(i), mainTextbox.getTextX(i-page*6-2), mainTextbox.getTextY(1));
					}
					if(page > 0){
						batch.draw(arrowLeft, mainTextbox.x+mainTextbox.getBorder(), mainTextbox.y+mainTextbox.getBorder(), 16, 16);
					}
					if(page*6+6<options.size()){
						batch.draw(arrowRight, mainTextbox.x+mainTextbox.getWidth()-mainTextbox.getBorder()-16, mainTextbox.y+mainTextbox.getBorder(), 16, 16);
					}
					float tx = mainTextbox.getTextX(Math.floorMod(pointer, 3)+1)-20;
					float ty = mainTextbox.getTextY(2-Math.floorMod(Math.floorDiv(pointer, 3),2))-18;
					batch.draw(arrowRight, tx, ty, 16, 16);
				}
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
				textbox.draw(batch, mainTextbox.x, mainTextbox.y, mainTextbox.getWidth(), mainTextbox.getHeight());
				textbox.draw(batch, leftTextbox.x, leftTextbox.y, leftTextbox.getWidth(), leftTextbox.getHeight());
				textbox.draw(batch, rightTextbox.x, rightTextbox.y, rightTextbox.getWidth(), rightTextbox.getHeight());
			}
			break;
		default:
			throw new IllegalStateException();
		}
	}
	
	private void inputHandle(){
		if(player.getHP() <= 0){
			activetbox = false;
			header = "You died.";
			return;
		}
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
						options.remove(pointer);
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
					movePointer();
				}
			default:
				break;
			}
			break;
		case COMBAT:
			if(activetbox){
				if(combatstate == CombatState.ACT && combat.whoseTurn()){
					switch(menu){
					case 0:
						for(Item item : playerItems){
							System.out.println(item.getName());
						}
						if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
							switch(options.get(pointer)){
							case "ATTACK":
								activetbox = false;
								header = "You attack " + enemy.getName() + " for " + enemy.damage(player.getAttackdmg()) + " damage";
								combatstate = CombatState.TURN_BEGIN;
								activeEffects = combat.turnBegin();
								break;
							case "WAIT":
								header = "You wait.";
								combatstate = CombatState.TURN_BEGIN;
								activeEffects = combat.turnBegin();
								activetbox = false;
								break;
							case "ABILITIY":
								menu = 1;
								pointer = 0;
								setCombatOptions();
								break;
							case "ITEM":
								menu = 2;
								pointer = 0;
								setCombatOptions();
								break;
							default:
								throw new IllegalStateException();
							}
							return;
						}
						movePointer();
						break;
					case 1:
						if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
							activeAbility = availableAbilities.get(pointer);
							header = "You use " + activeAbility.getName() + "!";
							player.spendMana(activeAbility.getMana());
							enemy.damage(activeAbility.getDMG());
							player.heal(activeAbility.getHealing());
							combat.playerAct(activeAbility);
							combatstate = CombatState.TURN_BEGIN;
							activetbox = false;
							activeEffects = combat.turnBegin();
							return;
						}
						movePointer();
						break;
					case 2:
						if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
							header = "You use " + options.get(pointer) + "!";
							enemy.damage(playerItems.get(pointer).getDamage());
							player.heal(playerItems.get(pointer).getHealing());
							combat.playerUseItem(playerItems.get(pointer));
							canUseItems = false;
							options.remove(pointer);
							player.getItems().remove(playerItems.get(pointer));
							playerItems.remove(pointer);
							activetbox = false;
							/*menu = 0;
							pointer = 0;
							setCombatOptions();*/
							return;
						}
						movePointer();
						break;
					default:
						throw new IllegalStateException();
					}
				}
				else
					throw new IllegalStateException();
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
				if(enemy.getHP() <= 0){
					if(header == enemy.getName() + " dies. You win!"){
						state=GameState.OVERWORLD;
						inputReady = false;
					}
					else{
						header = enemy.getName() + " dies. You win!";
						activetbox = false;
					}
					return;
				}
				switch(combatstate){
				case TURN_BEGIN:
					if(activeEffects.isEmpty()){
						combatstate = CombatState.ACT;
						if(combat.whoseTurn()){
							activetbox = true;
							header = "";
							pointer = 0;
							menu = 0;
							setCombatOptions();
							canUseItems = true;
						}
						else{
							activeAbility = combat.enemyAct();
							if(activeAbility.getName() == "Attack"){
								header = enemy.getName() + " attacks for " + player.damage(enemy.getAttackdmg()) + "!";
								combatstate = CombatState.TURN_BEGIN;
								activeEffects = combat.turnBegin();
							}
							else
								throw new IllegalStateException();
						}
					}
					else{
						if(activeEffects.get(0).damage > 0){
							if(activeEffects.get(0).ifPlayer){
								header = "You take " + player.damage(activeEffects.get(0).damage) + " damage.";
							}
							else{
								header = enemy.getName() + " takes " + enemy.damage(activeEffects.get(0).damage) + " damage.";
							}
						}
						if(activeEffects.get(0).healing > 0){
							if(activeEffects.get(0).ifPlayer){
								header = "You take " + player.heal(activeEffects.get(0).healing) + " healing.";
							}
							else{
								header = enemy.getName() + " takes " + enemy.heal(activeEffects.get(0).healing) + " healing.";
							}
						}
						activeEffects.remove(0);
					}
					break;
				case ACT:
					if(combat.whoseTurn()){
						activetbox = true;
						header = "";
						pointer = 0;
						menu = 0;
						setCombatOptions();
						
					}
					break;
				}
			}
			break;
		default:
			throw new IllegalStateException();
		}
	}
	
	private void movePointer(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
			if(Math.floorMod(pointer, 3) == 2 && pointer+4<options.size()){
				pointer+=4;
			}
			else if(pointer+1<options.size()){
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
			if(Math.floorMod(pointer, 6) < 3 && pointer+3<options.size()){
				pointer+=3;
			}
			return;
		}
	}
	
	private void setCombatOptions(){
		switch(menu){
		case 0:
			options = new ArrayList<String>();
			options.add("ATTACK");
			options.add("WAIT");
			if(canUseItems && playerItems.isEmpty() == false)
				options.add("ITEM");
			availableAbilities = new ArrayList<Ability>();
			for(Ability ability : player.getAbilities()){
				if(ability.getMana() <= player.getMana())
					availableAbilities.add(ability);
			}
			if(player.getAbilities().isEmpty() == false)
				options.add("ABILITY");
			break;
		case 1:
			options = new ArrayList<String>();
			availableAbilities = new ArrayList<Ability>();
			for(Ability ability : player.getAbilities()){
				if(ability.getMana() <= player.getMana()){
					availableAbilities.add(ability);
					options.add(ability.getName());
				}
			}
			break;
		case 2:
			options = new ArrayList<String>();
			for(Consumable item : playerItems){
				options.add(item.getName());
			}
		}
	}
	
	private void movePlayer(){
		if(canMove(movingX) == false)
			movingX = Direction.NULL;
		if(canMove(movingY) == false)
			movingY = Direction.NULL;
		if(movingX != Direction.NULL || movingY != Direction.NULL){
			if(Math.random() < Gdx.graphics.getDeltaTime()*playerSpeed/tilesize *1/20){
				enemyApproaching = true;
			}
		}
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
			switch(tile.getType()){
			case chest:
				if(tile.getItems().isEmpty()){
					activetbox = false;
					pointer = 32;
					header = "The chest is empty.";
				}
				else{
					activetbox = true;
					pointer = 0;
					header = "You find items in the chest!";
					options = new ArrayList<String>();
					for(Item item : tile.getItems()){
						options.add(item.getName());
					}
				}
				break;
			default:
				throw new IllegalStateException();
			}
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
	
	private void initCombat(){
		enemyApproaching = false;
		combat = new Combat(player,new Rat());
		enemy = combat.getEnemy();
		playerItems = new ArrayList<Consumable>();
		for(Item item : player.getItems()){
			if(item instanceof Consumable)
				playerItems.add((Consumable)item);
		}
		state = GameState.COMBAT;
		inputReady = false;
		header = combat.getEnemy().getName() + " attacks!";
		activetbox = false;
		combatstate = CombatState.TURN_BEGIN;
		activeEffects = combat.turnBegin();
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
