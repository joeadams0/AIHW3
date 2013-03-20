package Assignment3;
public class HarvestGoldAction implements StripsAction{
	public HarvestGoldAction(){
	}
	
	public boolean precondition(State state){
		GoldMine m = state.findClosestMine();
		Peasant p = state.getPeasant();
		//System.out.println("Close enough: (" + m.getX() + ", " + m.getY() + "), ("+ p.getX()+", "+p.getY()+")");
		return !(p.hasCargo()) && Math.abs(m.getX() - p.getX())<=1 && Math.abs(m.getY() - p.getY())<=1 && m.getGold() > 0;
	}
	public State postcondition(State state){
		State newState = state.clone();
		newState.getPeasant().setGold(true);
		GoldMine mine = newState.findClosestMine();
		mine.setGold(mine.getGold() - 100);
		return newState;
	}
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Harvest Gold, precondition: next to a GoldMine that has Gold and peasant isnt carrying anything, postcondition: mine - 100 wood and peasant carrying gold";
	}
}