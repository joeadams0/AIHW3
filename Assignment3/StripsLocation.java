package Assignment3;
public abstract class StripsLocation{
	protected int X;
	protected int Y;

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
	protected boolean isAtLocation(int x, int y){
		return Math.abs(X - x)<=1 && Math.abs(Y - y)<=1; 
	}
	public abstract boolean precondition(State state);
	public abstract String toString();
}