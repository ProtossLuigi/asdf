package levels;

public class Attack extends Ability {
	
	public Attack(Creature user){
		this.damage = user.attackdmg;
		this.healing = 0;
		this.specEffect = false;
	}

}
