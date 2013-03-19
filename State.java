import java.util.List;
public class State{
	private List<GoldMine> GoldMines;
	private List<Forest> Forests;
	private int Gold;
	private int Wood;
	private Peasant peasant;
	
	public State(List<GoldMine> mines, List<Forest> forests, int gold, int wood, Peasant p){
		GoldMines = mines;
		Forests = forests;
		Gold = gold;
		Wood = wood;
		peasant = p;
	}
	public List<GoldMine> getGoldMines(){
		return GoldMines;
	}
	
	public List<Forest> getForests(){
		return Forests;
	}
	
	public Peasant getPeasant(){
		return peasant;
	}
	
	public int getGold(){
		return Gold;
	}
	
	public int getWood(){
		return Wood;
	}
	
	public int heuristic(){
		return 0;
	}
	
	public boolean isGoalState(int GoalGold, int GoalWood){
		return Gold == GoalGold && Wood == GoalWood;
	}
	
	public boolean equals(State state){
		List<GoldMine> otherMines = state.getGoldMines();
		List<Forest> otherForests = state.getForests();
		for(int i = 0; i < GoldMines.size(); i++){
			if(!(GoldMines.get(i).equals(otherMines.get(i))))
				return false;
		}
		for(int i = 0; i < Forests.size(); i++){
			if(!(Forests.get(i).equals(otherForests.get(i))))
				return false;
		}
		if(Gold != state.getGold())
			return false;
		if(Wood != state.getWood())
			return false;
		if(!(peasant.equals(state.getPeasant()))){
			return false;
		}
		return true;
	}
}