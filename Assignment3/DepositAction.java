package Assignment3;
import java.lang.Math;
public class DepositAction implements StripsAction{
	public DepositAction(){
	}
	
	// Has cargo, and is near townhall
	public boolean precondition(State state){
		Townhall t = state.getTownhall();
		Peasant p = state.getPeasant();
		return p.hasCargo() && Math.abs(t.getX() - p.getX())<=1 && Math.abs(t.getY() - p.getY())<=1;
	}
	
	public State postcondition(State state){
		State newState = state.clone();
		if(newState.getPeasant().hasGold()){
			newState.setGold(newState.getGold()+100);
		}
		else if(newState.getPeasant().hasWood()){
			newState.setWood(newState.getWood()+100);
		}
		newState.getPeasant().setGold(false);
		newState.getPeasant().setWood(false);
		//System.out.println("OLD STATE:\n" + state + "\nNEW STATE\n" + newState + "\n");
		return newState;
	}
	
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Action:\nDeposit, precondition: next to townhall and carrying something, postcondition: townhall + 100 of either wood or gold and peasant has no cargo";
	}
}