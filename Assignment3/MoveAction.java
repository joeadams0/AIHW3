package Assignment3;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
public class MoveAction implements StripsAction{
	
	private StripsLocation Location;

	public MoveAction(StripsLocation Location){
		this.Location = Location;
	}
	
	public StripsLocation getLocation(){
		return Location;
	}

	// Is not at the Location
	public boolean precondition(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		return peasants.size()>0;
	}
	// Moves to the Location
	public State postcondition(State state){
		State newState = state.clone();
		List<Peasant> peasants = findEligablePeasants(newState);
		Peasant p = peasants.get(0);
		p.setPosition(Location.getX(), Location.getY());
		return newState;
	}
	// Pass in same state as precondition
	// Calculate distance to location
	public int getMakespan(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		Peasant p = peasants.get(0);
		int makespan = Math.abs(Location.getX()-p.getX());
		if(Math.abs(Location.getY()-p.getY()) < makespan){
			makespan = Math.abs(Location.getY()-p.getY());
		}
		return makespan;
	}
	public String toString(){
		return "Move to: "+Location.toString()+", precondition: !AtLocation(Peasant, Destination) , postcondition: AtLocation(Peasant, Destination)";
	}
	
	private boolean isNotAtLocation(int x, int y){
		return !Location.isAtLocation(x,y);
	}
	
	public List<Peasant> findEligablePeasants(State state){
		List<Peasant> peasants = state.getPeasants();
		List<Peasant> eligable = new ArrayList<Peasant>();
		ListIterator<Peasant> itr = peasants.listIterator();
		Peasant p;
		while(itr.hasNext()){
			p = itr.next();
			boolean precond = eligablePeasant(p, state);
			if(precond){
				eligable.add(p);
			}
		}
		return eligable;
	}
	
	private boolean eligablePeasant(Peasant p, State state){
		GoldMine m = state.closestGold(p.getX(), p.getY());
		boolean precond = isNotAtLocation(p.getX(), p.getY());
		return precond;
	}

}