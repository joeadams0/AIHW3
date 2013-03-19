public class GoldMine{
	private int X;
	private int Y;
	private int Gold;
	
	public GoldMine(int X, int Y, int Gold){
		this.X = X;
		this.Y = Y;
		this.Gold = Gold;
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public int getGold(){
		return Gold;
	}
	
	public void setX(int X){
		this.X = X;
	}
	public void setY(int Y){
		this.Y = Y;
	}
	public void setPosition(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}
	public void setGold(int Gold){
		this.Gold = Gold;
	}
	
	public boolean equals(GoldMine g){
		return X == g.getX() && Y == g.getY() && Gold == g.getGold();
	}
}