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
		layout[10][8] = new Tile(TileType.chest);
		layout[10][8].addItem(new Consumable("aaa"));
		layout[10][8].addItem(new Consumable("aab"));
		layout[10][8].addItem(new Consumable("adgffg"));
		layout[10][8].addItem(new Consumable("shit"));
		layout[10][8].addItem(new Consumable("aadsf"));
		layout[10][8].addItem(new Consumable("aa"));
		layout[10][8].addItem(new Consumable("rytaa"));
		layout[10][8].addItem(new Consumable("aaetg"));
		layout[10][8].addItem(new Consumable("aaa"));
		layout[10][8].addItem(new Consumable("asa"));
		layout[10][8].addItem(new Consumable("ada"));
		layout[10][8].addItem(new Consumable("aar"));
		layout[10][8].addItem(new Consumable("afdhga"));
		layout[10][8].addItem(new Consumable("aar"));
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
