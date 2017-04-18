package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import levels.Ability;
import levels.AbilityEffect;
import levels.Consumable;
import levels.Enemy;
import levels.Player;

public class Combat {
	
	private Player player;
	private int playerTurns;
	private int enemyTurns;
	private Enemy enemy;
	private boolean yourTurn;
	private List<AbilityEffect> standbyEffects;
	

	public Combat(Player player,Enemy enemy){
		this.player = player;
		this.enemy = enemy;
		this.playerTurns = 0;
		this.enemyTurns = 0;
		this.yourTurn = false;
		standbyEffects = new ArrayList<AbilityEffect>();
	}
	
	public List<AbilityEffect> turnBegin(){
		List<AbilityEffect> effects = new ArrayList<AbilityEffect>();
		yourTurn = (yourTurn == false);
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
					standbyEffects.remove(effect);
				}
				
			}
		}
		return effects;
	}
	
	public void playerAct(Ability ability){
		standbyEffects.addAll(ability.getEffects());
	}
	
	public void playerUseItem(Consumable item){
		standbyEffects.addAll(item.getEffects());
	}
	
	public Ability enemyAct(){
		Ability ability = enemy.act(enemyTurns, player.getHP());
		standbyEffects.addAll(ability.getEffects());
		return ability;
	}
	
	public void damage(){
		
	}
	
	public void heal(){
		
	}
	
	public Enemy getEnemy(){
		return enemy;
	}
	
	public boolean whoseTurn(){
		return yourTurn;
	}
}
