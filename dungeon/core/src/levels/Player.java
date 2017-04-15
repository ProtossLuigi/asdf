package levels;

import java.util.ArrayList;
import java.util.Optional;

public class Player extends Creature {

	public int coordx;
	public int coordy;
	public Direction facing;
	
	private int lvl;
	private int exp;
	private int maxmana;
	private int mana;
	
	private ArrayList<Item> items;
	private Optional<Armor> armor;
	private Optional<Weapon> weapon;
	
	public Player(int coordx,int coordy,Direction facing){
		this.lvl = 1;
		this.maxhp = 20;
		this.hp = maxhp;
		this.maxmana = 10;
		this.mana = maxmana;
		this.coordx=coordx;
		this.coordy=coordy;
		this.facing=facing;
		items = new ArrayList<Item>();
	}
	
	public void loot(Item item){
		items.add(item);
	}
}
