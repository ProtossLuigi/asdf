package levels;

import com.badlogic.gdx.graphics.Texture;

public enum TileType {
	
	ground(true,false),
	stone(false,false);
	
	boolean walkable;
	boolean interactable;
	Texture texture;
	
	TileType(boolean walkable,boolean interactable){
		this.walkable=walkable;
		this.interactable=interactable;
	}
	public Texture gettexture(){
		return texture;
	}
	
	public boolean getWalkable(){
		return walkable;
	}
	
	public boolean getInteractible(){
		return interactable;
	}
}
