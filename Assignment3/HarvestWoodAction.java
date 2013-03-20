package Assignment3;
public class HarvestWoodAction implements StripsAction{
	public HarvestWoodAction(){
	}
	
	// Doesnt have cargo, is close to the wood, forest has enough wood
	public boolean precondition(State state){
		Forest f = state.findClosestForest();
		Peasant p = state.getPeasant();
		return !(p.hasCargo()) && Math.abs(f.getX() - p.getX())<=1 && Math.abs(f.getY() - p.getY())<=1 && f.getWood() >= 100;
	}
	public State postcondition(State state){
		State newState = state.clone();
		newState.getPeasant().setWood(true);
		Forest forest = newState.findClosestForest();
		forest.setWood(forest.getWood() - 100);
		return newState;
	}
	public int getMakespan(State state){
		return 1;
	}

	public String toString(){
		return "Harvest Wood, precondition: next to a forest that has wood and peasant isnt carrying anything, postcondition: forest - 100 wood and peasant carrying wood";
	}
}