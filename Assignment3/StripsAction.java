package Assignment3;
import java.util.List;
public interface StripsAction{
	public boolean precondition(State state);
	public State postcondition(State state);
	public int getMakespan(State state);
	public List<Peasant> findEligablePeasants(State state);
}