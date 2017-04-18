package levels;

import java.util.ArrayList;

public class Rat extends Enemy {
	
	public Rat(){
		maxhp = 10;
		hp = 10;
		attackdmg = 3;
		this.name = "Rat";
	}
	
	public Ability act(int turn,int playerHP){
		return new Attack(this);
	}
}
