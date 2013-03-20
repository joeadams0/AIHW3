package Assignment3;
import java.lang.Math;
import java.util.List;
public class MoveToTownhallAction implements StripsAction{
	public MoveToTownhallAction(){
	}
	// Peasant has cargo
	public boolean precondition(State state){
		Peasant p = state.getPeasant();
		Townhall t = state.getTownhall();
		return p.hasCargo() && ((Math.abs(t.getX() - p.getX()) + (Math.abs(t.getX() - p.getY()))>2));
	}
	
	// Moves to the townhall
	public State postcondition(State state){
		State newState = state.clone();
		Peasant p = newState.getPeasant();
		Townhall t = state.getTownhall();
		p.setPosition(t.getX(), t.getY());
		//System.out.println("OLD STATE(townhall):\n" + state + "\nNEW STATE\n" + newState + "\n");
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
		return "Move to the townhall, precondition: peasant has cargo and peasant not at townhall, postcondition: peasant is at the townhall";
	}
	
}