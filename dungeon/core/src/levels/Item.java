package levels;

public abstract class Item {
	
	//int id;
	protected String name;
	
	public Item(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
