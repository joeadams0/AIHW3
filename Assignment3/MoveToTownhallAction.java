package Assignment3;
import java.lang.Math;
import java.util.List;
public class MoveToTownhallAction implements StripsAction{
	public MoveToTownhallAction(){
	}
	// No precondition
	public boolean precondition(State state){
		return true;
	}
	
	// Moves to the townhall
	public State postcondition(State state){
		State newState = state.clone();
		Peasant p = newState.getPeasant();
		Townhall t = state.getTownhall();
		p.setPosition(t.getX(), t.getY());
		return newState;
	}
	
	// Pass in same state as precondition
	// Calculate distance to townhall
	public int getMakespan(State state){
		Peasant p = state.getPeasant();
		Townhall t = state.getTownhall();
		int makespan = Math.abs(t.getX()-p.getX());
		if(Math.abs(t.getY()-p.getY()) < makespan){
			makespan = Math.abs(t.getY()-p.getY());
		}
		return makespan;
	}
	public String toString(){
		return "Move to the townhall, precondition: none, postcondition: peasant is at goldmine";
	}
	
}