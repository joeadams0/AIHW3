public class Peasant{
	private int X;
	private int Y;
	private boolean Cargo;
	
	public Peasant(int X, int Y){
		this.X = X;
		this.Y = Y;
		this.Cargo = false;
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public boolean hasCargo(){
		return Cargo;
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
	
	public void setCargo(boolean Cargo){
		this.Cargo = Cargo;
	}
	
	public boolean equals(Peasant p){
		return X == p.getX() && Y == p.getY() && Cargo == p.hasCargo();
	}
}