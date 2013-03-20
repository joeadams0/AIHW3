package Assignment3;
import java.lang.Math;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
public class DepositAction implements StripsAction{
	public DepositAction(){
	}
	
	// Has cargo, and is near townhall
	public boolean precondition(State state){
		List<Peasant> peasants = findEligablePeasants(state);
		return peasants.size()>0;
	}
	
	public State postcondition(State state){
		State newState = state.clone();
		List<Peasant> peasants = findEligablePeasants(newState);
		Peasant p = peasants.get(0);
		if(p.hasGold()){
			newState.setGold(newState.getGold()+100);
		}
		else if(p.hasWood()){
			newState.setWood(newState.getWood()+100);
		}
		p.setGold(false);
		p.setWood(false);
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Deposit, precondition: AtLocation(Peasant, Townhall) ^ HasCargo(Peasant) , postcondition: townhall + 100 of either wood or gold ^ !HasCargo(Peasant)";
	}
	
	@Override
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