package Assignment3;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
public class BuildPeasantAction{
	public boolean precondition(State state){
		List<Peasant> peasants = state.getPeasants();
		if(peasants.size()>2){
			return false;
		}
		else{
			return state.getGold()>=400;
		}
	}
	public State postcondition(State state){
		State newState = state.clone();
		Townhall t = newState.getTownhall();
		newState.addPeasant(new Peasant(t.getX(), t.getY()));
		newState.setGold(newState.getGold()-400);
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}
	
	public List<Peasant> findEligablePeasants(State state){
		return new ArrayList<Peasant>();
	}
}