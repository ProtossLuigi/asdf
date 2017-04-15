package levels;

public abstract class Creature {

	protected int maxhp;
	protected int hp;
	protected int baseArmor;
	protected int armor;
	protected int attackdmg;
	
	public boolean damage(int dmg){
		dmg-=armor;
		hp-=dmg;
		if(hp <= 0)
			return false;
		else
			return true;
	}
	
	public void heal(int heal){
		hp+=heal;
		if(hp > maxhp)
			hp = maxhp;
	}
}
