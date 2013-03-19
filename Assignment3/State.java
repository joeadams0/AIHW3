package Assignment3;
import java.util.List;
import java.util.ArrayList;
public class State{
	private List<GoldMine> GoldMines;
	private List<Forest> Forests;
	private int Gold;
	private int Wood;
	private Peasant peasant;
	private Townhall townhall;
	
	public State(List<GoldMine> mines, List<Forest> forests, int gold, int wood, Peasant p, Townhall townhall){
		GoldMines = mines;
		Forests = forests;
		Gold = gold;
		Wood = wood;
		peasant = p;
		this.townhall = townhall;
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
	
	public Townhall getTownhall(){
		return townhall;
	}
	
	public void setGold(int gold){
		Gold = gold;
	}
	
	public void setWood(int wood){
		Wood = wood;
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
		if(!(peasant.equals(state.getPeasant())))
			return false;			
		if(!(townhall.equals(state.getTownhall())))
			return false;
			
		return true;
	}
	
	public State clone(){
		List<GoldMine> mines = new ArrayList<GoldMine>();
		List<Forest> forests = new ArrayList<Forest>();
		int gold = Gold;
		int wood = Wood;
		Peasant p = peasant.clone();
		Townhall t = townhall.clone();
		
		for(GoldMine mine : GoldMines){
			mines.add(mine.clone());
		}
		for(Forest forest : Forests){
			forests.add(forest.clone());
		}
		
		return new State(mines, forests, gold, wood, p, t);
	}
	
	public GoldMine findClosestMine(){
		Peasant p = peasant;
		List<GoldMine> mines = GoldMines;
		int x = p.getX();
		int y = p.getY();
		GoldMine closestMine = mines.get(0);
		for(GoldMine mine: mines){
			if((Math.abs(mine.getX() - x)+Math.abs(mine.getX()-y))<(Math.abs(closestMine.getX() - x)+Math.abs(closestMine.getY()-y))){
				closestMine = mine;
			}
		}
		return closestMine;
	}
	
	public Forest findClosestForest(){
		Peasant p = peasant;
		List<Forest> forests = Forests;
		int x = p.getX();
		int y = p.getY();
		Forest closestForest = forests.get(0);
		for(Forest forest: forests){
			if((Math.abs(forest.getX() - x)+Math.abs(forest.getX()-y))<(Math.abs(closestForest.getX() - x)+Math.abs(closestForest.getY()-y))){
				closestForest = forest;
			}
		}
		return closestForest;
	}
	
	public String toString(){
		String str = "State:\n";
		for(GoldMine mine : GoldMines){
			str = str + mine.toString() + "\n";
		}
		str = str + "\n";
		for(Forest forest : Forests){
			str = str + forest.toString() + "\n";
		}
		str = str + "\n";
		
		str = str + townhall.toString() + "\n";
		str = str + peasant.toString() + "\n";
		str = str + "Gold: " + Gold + "\n";
		str = str + "Wood: " + Wood + "\n";
		
		return str;
	}
}