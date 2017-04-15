package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Textbox1 {
	
	private float border;
	protected float x;
	protected float y;
	private float fontheight;
	private float tilesize = 64;
	private GameState currentState;
	private boolean ready;
	
	public Textbox1(BitmapFont font,float tilesize){
		ready = false;
		x = 0;
		y = 0;
		border = 8;
		fontheight = 3;
	}
	
	public void open(GameState mode){
		switch(mode){
		case TILEINTERACTION:
			if(y == 0){
				x = (Gdx.graphics.getWidth()-32)/2;
				y = Gdx.graphics.getHeight()/5;
			}
			if(y > Gdx.graphics.getHeight()/20){
				y -= Gdx.graphics.getHeight()*Gdx.graphics.getDeltaTime();
				if(y < Gdx.graphics.getHeight()/20)
					y = Gdx.graphics.getHeight()/20;
			}
			if(y == Gdx.graphics.getHeight()/20 && x > Gdx.graphics.getWidth()/32){
				x -= ((Gdx.graphics.getWidth()-32)/2-Gdx.graphics.getWidth()/32)*Gdx.graphics.getDeltaTime()*4;
				if(x <= Gdx.graphics.getWidth()/32){
					x = Gdx.graphics.getWidth()/32;
					currentState = mode;
					ready = true;
				}
			}
			break;
		default:
			throw new IllegalStateException();
		}
	}
	
	public void close(){
		switch(currentState){
		case TILEINTERACTION:
			if(x < (Gdx.graphics.getWidth()-32)/2){
				x += ((Gdx.graphics.getWidth()-32)/2-Gdx.graphics.getWidth()/32)*Gdx.graphics.getDeltaTime()*4;
				if(x > (Gdx.graphics.getWidth()-32)/2)
					x = (Gdx.graphics.getWidth()-32)/2;
			}
			if(x >= (Gdx.graphics.getWidth()-32)/2 && y < Gdx.graphics.getHeight()/5){
				y += Gdx.graphics.getHeight()*Gdx.graphics.getDeltaTime();
			}
			if(y >= Gdx.graphics.getHeight()/5){
				x = 0;
				y = 0;
				ready = false;
			}
			break;
		default:
			throw new IllegalStateException();
		}
	}
	
	public boolean getReady(){
		return ready;
	}
	
	public float getWidth(){
		return Gdx.graphics.getWidth()-2*x;
	}
	
	public float getHeight(){
		return (Gdx.graphics.getHeight()-tilesize)/2-2*y;
	}
	
	public float getTextX(int n){
		switch(n){
		case 1:
			return x+2*border+16;
		case 2:
			return x+this.getWidth()/3+2*border+16;
		case 3:
			return x+2*this.getWidth()/3+2*border+16;
		default:
			return x+border;
		}
	}
	
	public float getTextY(int n){
		switch(n){
		case 1:
			return y+2*border+32;
		case 2:
			return y+2*border+64;
		default:
			return y+this.getHeight()-fontheight-border;
		}
	}
	
	public float getBorder(){
		return border;
	}
	
}
