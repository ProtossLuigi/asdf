package levels;

import java.util.ArrayList;

public class Attack extends Ability {
	
	public Attack(Creature user){
		this.name = "Attack";
		this.damage = user.attackdmg;
		this.healing = 0;
		this.effects = new ArrayList<AbilityEffect>();
	}

}
