package Assignment3;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
public class HarvestWoodAction3 implements StripsAction{
	public HarvestWoodAction3(){
	}
	
	// Doesnt have cargo, is close to the wood, forest has enough wood
	public boolean precondition(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		return peasants.size()>2;
	}
	
	public State postcondition(State state){
		State newState = state.clone();
		List<Peasant> peasants = findEligablePeasants(newState);
		for(int i = 0; i< 3; i++){
			Peasant p = peasants.get(i);
			p.setWood(true);
			Forest forest = newState.closestForest(p.getX(), p.getY());
			forest.setWood(forest.getWood() - 100);
		}
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Harvest Wood3, precondition: for 3 peasants: AtLocation(PeasantI, ForestI) ^ HasWood(ForestI) ^ !HasCargo(PeasantI) , postcondition: forestI - 100 wood ^ HasCargo(PeasantI)";
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
		Forest f = state.closestForest(p.getX(), p.getY());
		boolean precond = f.getWood() >= 100 && !(p.hasCargo()) && f.isAtLocation(p.getX(), p.getY());
		return precond;
	}
}