package Assignment3;
public class Forest extends StripsLocation{
	private int Wood;
	
	public Forest(int X, int Y, int Wood){
		this.X = X;
		this.Y = Y;
		this.Wood = Wood;
	}
	
	public int getWood(){
		return Wood;
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

	@Override
	public boolean precondition(State state){
		return true;
	}
}