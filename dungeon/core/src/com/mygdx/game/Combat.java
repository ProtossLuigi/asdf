package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import levels.AbilityEffect;
import levels.Enemy;
import levels.Player;

public class Combat {
	
	private int playerTurns;
	private int enemyTurns;
	private boolean yourTurn;
	private List<AbilityEffect> standbyEffects;
	

	public Combat(Player player,Enemy enemy){
		this.playerTurns = 0;
		this.enemyTurns = 0;
		this.yourTurn = true;
		standbyEffects = new ArrayList<AbilityEffect>();
	}
	
	public List<AbilityEffect> turnBegin(){
		List<AbilityEffect> effects = new ArrayList<AbilityEffect>();
		if(yourTurn){
			playerTurns++;
			for(AbilityEffect effect : standbyEffects){
				if(effect.trigger == 0 && effect.turn == playerTurns && effect.ifPlayer){
					effects.add(effect);
				}
				
			}
		}
		else{
			enemyTurns++;
			for(AbilityEffect effect : standbyEffects){
				if(effect.trigger == 0 && effect.turn == enemyTurns && effect.ifPlayer == false){
					effects.add(effect);
				}
				
			}
		}
		return effects;
	}
	
	public void act(){
		
	}
	
	public void turnEnd(){
		
	}
	
	public void damage(){
		
	}
	
	public void heal(){
		
	}
}
