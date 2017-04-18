package levels;

public class Floor {
	
	Tile[][] layout;
	int level;
	int width;
	int height;
	
	 public Floor(int nr,boolean test){
		this.level=nr;
		if(test==true){
			this.width=24;
			this.height=16;
			this.layout = new Tile[width][height];
			for(int y=0;y<16;y++){
				for(int x=0;x<24;x++){
					 layout[x][y] = new Tile(TileType.stone);
				}
			}
			for(int y=4;y<12;y++){
				for(int x=4;x<20;x++){
					layout[x][y] = new Tile(TileType.ground);
				}
			}
		}
		layout[4][5] = new Tile(TileType.chest);
		layout[4][5].addItem(new Grenade());
	}
	public int getheight(){
		return this.height;
	}
	public int getwidth(){
		return this.width;
	}
	public Tile getTile(int x, int y){
		return layout[x][y];
	}
}
