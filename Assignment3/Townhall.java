package Assignment3;
public class Townhall extends StripsLocation{
	
	public Townhall(int X, int Y){
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

	@Override
	public boolean precondition(State state){
		return true;
	}
}