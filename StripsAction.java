public interface StripsAction{
	public boolean precondition(State state);
	public State postcondition(State state);
	public int getMakespan(State state);
}