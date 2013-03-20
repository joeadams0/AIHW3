package Assignment3;
public class GoldMine extends StripsLocation{
	private int Gold;
	
	public GoldMine(int X, int Y, int Gold){
		this.X = X;
		this.Y = Y;
		this.Gold = Gold;
	}
	
	public int getGold(){
		return Gold;
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
	
	public GoldMine clone(){
		return new GoldMine(X, Y, Gold);
	}
	
	public String toString(){
		return "GoldMine: "+ X + ", " + Y + ", " + Gold;
	}

	@Override
	public boolean precondition(State state){
		return true;
	}
}