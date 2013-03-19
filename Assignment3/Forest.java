package Assignment3;
public class Forest{
	private int X;
	private int Y;
	private int Wood;
	
	public Forest(int X, int Y, int Wood){
		this.X = X;
		this.Y = Y;
		this.Wood = Wood;
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public int getWood(){
		return Wood;
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
	public void setWood(int Wood){
		this.Wood = Wood;
	}
	
	public boolean equals(Forest f){
		return X == f.getX() && Y == f.getY() && Wood == f.getWood();
	}
	
	public Forest clone(){
		return new Forest(X, Y, Wood);
	}
	
	public String toString(){
		return "Forest: "+ X + ", " + Y + ", " + Wood;
	}
}