package Assignment3;
public class Peasant{
	private int X;
	private int Y;
	private boolean Wood;
	private boolean Gold;
	
	public Peasant(int X, int Y){
		this.X = X;
		this.Y = Y;
		this.Wood = false;
		this.Gold = false;
	}
	
	public Peasant(int X, int Y, boolean HasWood, boolean HasGold){
		this.X = X;
		this.Y = Y;
		this.Wood = HasWood;
		this.Gold = HasGold;
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public boolean hasCargo(){
		return Wood || Gold;
	}
	
	public boolean hasWood(){
		return Wood;
	}
	
	public boolean hasGold(){
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
	
	public void setWood(boolean wood){
		this.Wood = wood;
	}
	
	public void setGold(boolean gold){
		this.Gold = gold;
	}
	
	public boolean equals(Peasant p){
		return X == p.getX() && Y == p.getY() && Gold == p.hasGold() && Wood == p.hasWood();
	}
	
	public Peasant clone(){
		return new Peasant(X, Y, Gold, Wood);
	}
	
	public String toString(){
		return "Peasant: "+ X + ", " + Y + ", " + Gold + ", " + Wood;
	}
}