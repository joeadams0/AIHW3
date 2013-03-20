package Assignment3;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
public class HarvestGoldAction3 implements StripsAction{
	public HarvestGoldAction3(){
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
			p.setGold(true);
			GoldMine mine = newState.closestGold(p.getX(), p.getY());
			mine.setGold(mine.getGold() - 100);
		}
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Harvest Gold3, precondition: for 3 peasants: AtLocation(PeasantI, MineI) ^ HasWood(MineI) ^ !HasCargo(PeasantI) , postcondition: mineI - 100 gold ^ HasCargo(PeasantI)";
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