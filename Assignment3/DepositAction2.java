package Assignment3;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
public class DepositAction2 implements StripsAction{
	public DepositAction2(){
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
			if(p.hasGold()){
				newState.setGold(newState.getGold()+100);
			}
			else if(p.hasWood()){
				newState.setWood(newState.getWood()+100);
			}
			p.setGold(false);
			p.setWood(false);
		}
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Deposit2, precondition: for 2 peasants: AtLocation(PeasantI, Townhall) ^ HasCargo(PeasantI) , postcondition: townhall + 100 of either wood or gold ^ !HasCargo(PeasantI)";
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
		Townhall t = state.getTownhall();
		boolean precond = p.hasCargo() && t.isAtLocation(p.getX(), p.getY());
		return precond;
	}
}