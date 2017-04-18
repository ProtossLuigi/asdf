package levels;

public abstract class Creature {

	protected int maxhp;
	protected int hp;
	protected int baseArmor;
	protected int armor;
	protected int attackdmg;
	
	public Creature(){
		baseArmor = 0;
		maxhp = 10;
		hp = maxhp;
		attackdmg = 1;
	}
	
	public int damage(int dmg){
		dmg-=armor;
		hp-=dmg;
		return dmg;
	}
	
	public int heal(int heal){
		if(hp+heal > maxhp){
			hp = maxhp;
			return maxhp-hp;
		}
		else{
			hp+=heal;
			return heal;
		}
	}
	
	public int getHP(){
		return hp;
	}
	
	public int getMaxHP(){
		return maxhp;
	}
	
	public int getAttackdmg() {
		return attackdmg;
	}
}
