package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Textbox3 {
	private float border;
	protected float x;
	protected float y;
	private float fontheight;
	private GameState currentState;
	private boolean ready;
	private BitmapFont font;
	
	public Textbox3(BitmapFont font,float tilesize){
		ready = false;
		x = 0;
		y = 0;
		border = 8;
		fontheight = 3;
		this.font = font;
	}
	
	public void open(GameState mode){
		if(mode == GameState.COMBAT || mode == GameState.MENU){
			if(x==0){
				x = Gdx.graphics.getWidth()/8-16+Gdx.graphics.getWidth()*3/4;
				y = Gdx.graphics.getHeight()*3/4-8;
			}
			if(x > 32+Gdx.graphics.getWidth()*3/4){
				x -= (Gdx.graphics.getWidth()/8-16-32)*Gdx.graphics.getDeltaTime()*3/20;
				if(x < 32+Gdx.graphics.getWidth()*3/4)
					x = 32+Gdx.graphics.getWidth()*3/4;
			}
			if(x == 32+Gdx.graphics.getWidth()*3/4 && y > Gdx.graphics.getHeight()/2+32){
				y -= ((Gdx.graphics.getHeight()*3/4-8)-(Gdx.graphics.getHeight()/2+32))*Gdx.graphics.getDeltaTime()/5;
				if(y <= Gdx.graphics.getHeight()/2+32){
					y = Gdx.graphics.getHeight()/2+32;
					currentState = mode;
					ready = true;
				}
			}
		}
	}
	
	public void close(){
		if(currentState == GameState.COMBAT || currentState == GameState.MENU){
			if(y < Gdx.graphics.getHeight()*3/4-8){
				y += ((Gdx.graphics.getHeight()*3/4-8)-(Gdx.graphics.getHeight()/2+32))*Gdx.graphics.getDeltaTime()/5;
			}
			if(y >= Gdx.graphics.getHeight()*3/4-8 && x < Gdx.graphics.getWidth()/8-16+Gdx.graphics.getWidth()*3/4){
				x += (Gdx.graphics.getWidth()/8-16-32)*Gdx.graphics.getDeltaTime()*3/20;
			}
			if(y >= Gdx.graphics.getHeight()*3/4-8 && x >= Gdx.graphics.getWidth()/8-16+Gdx.graphics.getWidth()*3/4){
				x = 0;
				y = 0;
				ready = false;
			}
		}
	}
	
	public boolean getReady(){
		return ready;
	}
	
	public float getWidth(){
		return Gdx.graphics.getWidth()/4-2*x;
	}
	
	public float getHeight(){
		return Gdx.graphics.getHeight()/2-2*(y-Gdx.graphics.getHeight()/2);
	}
	
	public float getTextX(){
		return x+border;
	}
	
	public float getTextY(int n){
		return y+this.getHeight()-fontheight-border-font.getLineHeight()*n;
	}
	
	public float getBorder(){
		return border;
	}
}
