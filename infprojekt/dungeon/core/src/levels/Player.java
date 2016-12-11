package levels;

public class Player {

	public int coordx;
	public int coordy;
	public Direction facing;
	
	public Player(int coordx,int coordy,Direction facing){
		this.coordx=coordx;
		this.coordy=coordy;
		this.facing=facing;
	}
	
	public boolean move(Direction way,Floor level){
		if(way != Direction.NULL)
			facing = way;
		switch(way){
		case UP:
			if((level.getTile(coordx,coordy+1)).getWalkable()==true){
				coordy++;
				return true;
			}
			else
				return false;
		case DOWN:
			if((level.getTile(coordx,coordy-1)).getWalkable()==true){
				coordy--;
				return true;
			}
			else
				return false;
		case LEFT:
			if((level.getTile(coordx-1,coordy)).getWalkable()==true){
				coordx--;
				return true;
			}
			else
				return false;
		case RIGHT:
			if((level.getTile(coordx+1,coordy)).getWalkable()==true){
				coordx++;
				return true;
			}
			else
				return false;
		default:
			return false;
		}
	}
}
