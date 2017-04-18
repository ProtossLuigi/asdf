package levels;

import java.util.List;

public abstract class Ability {
	
	String name;
	int damage;
	int healing;
	int mana;
	protected List<AbilityEffect> effects;
	
	public Ability(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public int getDMG(){
		return damage;
	}
	
	public int getHealing(){
		return healing;
	}
	
	public int getMana(){
		return mana;
	}
	
	public List<AbilityEffect> getEffects() {
		return effects;
	}
}
