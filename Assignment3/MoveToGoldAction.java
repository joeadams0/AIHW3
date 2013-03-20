package Assignment3;
import java.lang.Math;
import java.util.List;
public class MoveToGoldAction implements StripsAction{
	public MoveToGoldAction(){
	}
	// Has no cargo
	public boolean precondition(State state){
		Peasant p = state.getPeasant();
		GoldMine t = state.findClosestMine();
		return !state.getPeasant().hasCargo() && ((Math.abs(t.getX() - p.getX())>1 &&  (Math.abs(t.getX() - p.getY()))>1));
	}
	// Moves to the nearest gold mine
	public State postcondition(State state){
		State newState = state.clone();
		Peasant p = newState.getPeasant();
		GoldMine mine = state.findClosestMine();
		p.setPosition(mine.getX(), mine.getY());
		return newState;
	}
	// Pass in same state as precondition
	// Calculate distance to nearest Goldmine
	public int getMakespan(State state){
		Peasant p = state.getPeasant();
		GoldMine mine = state.findClosestMine();
		int makespan = Math.abs(mine.getX()-p.getX());
		if(Math.abs(mine.getY()-p.getY()) < makespan){
			makespan = Math.abs(mine.getY()-p.getY());
		}
		return makespan;
	}
	public String toString(){
		return "Move to nearest GoldMine, precondition: peasant has no cargo and must not be at the goldmine, postcondition: peasant is at goldmine";
	}

}