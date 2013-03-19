package Assignment3;
public class Townhall{
	private int X;
	private int Y;
	
	public Townhall(int X, int Y){
		this.X = X;
		this.Y = Y;
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
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
	
	public boolean equals(Townhall t){
		return X == t.getX() && Y == t.getY();
	}
	
	public Townhall clone(){
		return new Townhall(X, Y);
	}
	
	public String toString(){
		return "Townhall: "+ X + ", " + Y;
	}
}