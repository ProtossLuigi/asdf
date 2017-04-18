package levels;

import java.util.ArrayList;
import java.util.List;

public abstract class Consumable extends Item {
	
	int damage;
	int healing;
	List<AbilityEffect> effects;

	public Consumable() {
		effects = new ArrayList<AbilityEffect>();
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getHealing() {
		return healing;
	}
	
	public List<AbilityEffect> getEffects() {
		return effects;
	}
}
