package levels;

import java.util.Optional;

public class Tile {
	private TileType type;
	private Optional<Object> object;
	
	public Tile(TileType type){
		this.type = type;
		if(type.getInteractible()==false){
			this.object = Optional.empty();
		}
	}
	
	public Optional<Object> getObject() {
		return object;
	}

	public void setObject(Optional<Object> object) {
		this.object = object;
	}

	public TileType gettype(){
		return type;
	}
	
	public boolean getWalkable(){
		return type.getWalkable();
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
