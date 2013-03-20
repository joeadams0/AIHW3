package Assignment3;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
public class MoveAction2 implements StripsAction{
	private StripsLocation Location;
	public MoveAction2(StripsLocation Location){
		this.Location = Location;
	}
	public StripsLocation getLocation(){
		return Location;
	}
	// Doesnt have cargo, is close to the wood, forest has enough wood
	public boolean precondition(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		return peasants.size()>1;
	}
	
	public State postcondition(State state){
		State newState = state.clone();
		List<Peasant> peasants = findEligablePeasants(newState);
		for(int i = 0; i< 2; i++){
			Peasant p = peasants.get(i);
			p.setPosition(Location.getX(), Location.getY());
		}
		return newState;
	}
	
	public int getMakespan(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		int makespan = 0;
		for(Peasant p : peasants){
			int dist = Math.abs(Location.getX()-p.getX())+ Math.abs(Location.getY()-p.getY());
			if(dist < makespan){
				makespan =dist;
			}
		}
		return makespan;
	}

	public String toString(){
		return "Move to: "+Location.toString()+", precondition: for 2 Peasants: !AtLocation(PeasantI, Destination) , postcondition: AtLocation(PeasantI, Destination)";
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