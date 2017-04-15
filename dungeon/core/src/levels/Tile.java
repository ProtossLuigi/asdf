package levels;

import java.util.ArrayList;

public class Tile {
	private TileType type;
	private ArrayList<Item> items;
	
	public Tile(TileType type){
		this.type = type;
		if(type == TileType.chest){
			items = new ArrayList<Item>();
		}
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> object) {
		this.items = object;
	}
	
	public void addItem(Item item){
		items.add(item);
	}

	public TileType getType(){
		return type;
	}
	
	public boolean getWalkable(){
		return type.getWalkable();
	}
	
	public boolean getInteractible(){
		return type.getInteractible();
	}

	/*public static void main(String[] args) {
		Tile t = new Tile();
		Optional<Long> o = Optional.of(3L);
		System.out.println(o);
		System.out.println(o.map(x -> x * 3));
		o = Optional.empty();
		System.out.println(o);
		System.out.println(o.map(x -> x * 3));
	}*/
}
