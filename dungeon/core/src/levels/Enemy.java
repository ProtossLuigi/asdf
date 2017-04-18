package levels;

import java.util.List;

public abstract class Enemy extends Creature {
	
	protected String name;
	protected List<Ability> abilities;
	
	public Enemy(){
		
	}
	
	public String getName(){
		return name;
	}
	
	public abstract Ability act(int turn,int playerHP);
}
