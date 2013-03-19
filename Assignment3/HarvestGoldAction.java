package Assignment3;
public class HarvestGoldAction implements StripsAction{
	public HarvestGoldAction(){
	}
	
	public boolean precondition(State state){
		GoldMine m = state.findClosestMine();
		Peasant p = state.getPeasant();
		return !(p.hasCargo()) && Math.abs(m.getX() - p.getX())<=1 && Math.abs(m.getY() - p.getY())<=1 && m.getGold() >= 100;
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
}