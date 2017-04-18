package levels;

import java.util.ArrayList;

public class Grenade extends Consumable {
	
	public Grenade(){
		this.name = "Grenade";
		this.damage = 5;
		this.healing = 0;
		effects = new ArrayList<AbilityEffect>();
	}
}
