package Assignment3;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
public class HarvestGoldAction implements StripsAction{
	public HarvestGoldAction(){
	}
	
	// Doesnt have cargo, is close to the wood, forest has enough wood
	public boolean precondition(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		return peasants.size()>0;
	}
	
	public State postcondition(State state){
		State newState = state.clone();
		List<Peasant> peasants = findEligablePeasants(newState);
		Peasant p = peasants.get(0);
		p.setGold(true);
		GoldMine mine = newState.closestGold(p.getX(), p.getY());
		mine.setGold(mine.getGold() - 100);
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Harvest Gold, precondition: AtLocation(Peasant, Mine) ^ HasGold(Mine) ^ !HasCargo(Peasant), postcondition: Mine - 100 gold and HasCargo(Peasant)";
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
		boolean precond = !(p.hasCargo()) && m.isAtLocation(p.getX(), p.getY()) && m.getGold() >= 100;
		return precond;
	}
}