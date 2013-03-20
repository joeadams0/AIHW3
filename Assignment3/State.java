package Assignment3;
import java.util.List;
import java.util.ArrayList;
public class State{
	private List<GoldMine> GoldMines;
	private List<Forest> Forests;
	private int Gold;
	private int Wood;
	private List<Peasant> Peasants;
	private Townhall townhall;
	
	public State(List<GoldMine> mines, List<Forest> forests, int gold, int wood, List<Peasant> p, Townhall townhall){
		GoldMines = mines;
		Forests = forests;
		Gold = gold;
		Wood = wood;
		Peasants = p;
		this.townhall = townhall;
	}
	public List<GoldMine> getGoldMines(){
		return GoldMines;
	}
	
	public List<Forest> getForests(){
		return Forests;
	}
	
	public Peasant getPeasant(){
		return Peasants.get(0);
	}

	public List<Peasant> getPeasants(){
		return Peasants;
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
	
	
	public boolean isGoalState(int GoalGold, int GoalWood){
		return Gold >= GoalGold && Wood >= GoalWood;
	}
	
	public boolean equals(State state){
		List<GoldMine> otherMines = state.getGoldMines();
		List<Forest> otherForests = state.getForests();
		List<Peasant> otherPeasants = state.getPeasants();

		for(int i = 0; i < GoldMines.size(); i++){
			if(!(GoldMines.get(i).equals(otherMines.get(i))))
				return false;
		}
		for(int i = 0; i < Forests.size(); i++){
			if(!(Forests.get(i).equals(otherForests.get(i))))
				return false;
		}
		for(int i = 0; i < Peasants.size(); i++){
			if(!(Peasants.get(i).equals(otherPeasants.get(i))))
				return false;
		}
		if(Gold != state.getGold())
			return false;
		if(Wood != state.getWood())
			return false;
		if(!(Peasants.equals(state.getPeasants())))
			return false;			
		if(!(townhall.equals(state.getTownhall())))
			return false;
			
		return true;
	}
	
	public State clone(){
		List<GoldMine> mines = new ArrayList<GoldMine>();
		List<Forest> forests = new ArrayList<Forest>();
		List<Peasant> peasants = new ArrayList<Peasant>();
		int gold = Gold;
		int wood = Wood;
		Townhall t = townhall.clone();
		
		for(GoldMine mine : GoldMines){
			mines.add(mine.clone());
		}
		for(Forest forest : Forests){
			forests.add(forest.clone());
		}
		for(Peasant peasant : Peasants){
			peasants.add(peasant.clone());
		}
		
		return new State(mines, forests, gold, wood, peasants, t);
	}
	
	public GoldMine findClosestMine(){
		return closestGold(townhall.getX(), townhall.getY());
	}
	
	public Forest findClosestForest(){
		return closestForest(townhall.getX(), townhall.getY());
	}

	public GoldMine closestGold(int x, int y){
		List<GoldMine> mines = GoldMines;
		GoldMine closestMine = null;
		for(GoldMine mine: mines){
			if(!(mine.getGold()>=100)){
					continue;	
			}
			if(closestMine == null){
				closestMine = mine;
			}
			else if(dist(x, y, mine.getX(), mine.getY())<dist(x, y, closestMine.getX(), closestMine.getY())){
				closestMine = mine; 
			}
		}
		return closestMine;
	}

	public Forest closestForest(int x, int y){
		List<Forest> forests = Forests;
		Forest closestForest = null;
		for(Forest forest: forests){
			if(!(forest.getWood()>100)){
				continue;
			}
			if(closestForest == null){
				closestForest = forest;
			}
			else if(Math.max(Math.abs(forest.getX() - x),Math.abs(forest.getY()-y))<Math.max(Math.abs(closestForest.getX() - x),Math.abs(closestForest.getY()-y))){
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
		str = str + Peasants.toString() + "\n";
		str = str + "Gold: " + Gold + "\n";
		str = str + "Wood: " + Wood + "\n";
		
		return str;
	}
	
	public int heuristic(int goalGold, int goalWood){

		if(this.isGoalState(goalGold, goalWood)){
			return 0;
		}

		int heuristic = 0;
		int goldNeeded = goalGold - Gold;
		int woodNeeded = goalWood - Wood;

		if(goldNeeded <= 0 && woodNeeded <= 0){
			return heuristic;
		}
		
		//start new stuff
		int actionsRequired = 0;
		if(goldNeeded > 0)
			actionsRequired += (goldNeeded/100)*4;
		if(woodNeeded > 0)
			actionsRequired += (woodNeeded/100)*4;
		
		for(Peasant p : Peasants){
			if(p.hasWood()){
				if(woodNeeded <= 0){
					actionsRequired += 2;
				}
				else{
					actionsRequired -= 2;
				}
			}
			else if (p.hasGold()){
				if(goldNeeded <= 0){
					actionsRequired += 2;
				}
				else{
					actionsRequired -= 2;
				}
			}
		}
		heuristic = (int)Math.ceil((double)actionsRequired/(double)Peasants.size());
		/*
		if(goldNeeded<0){
			heuristic -= goldNeeded;
		}
		
		if(woodNeeded<0){
			heuristic -= woodNeeded;
		}
		if (peasant.hasWood()){
			heuristic += distToTownHall(peasant.getX(), peasant.getY())+1;
			woodNeeded -=100;
		}
		else if (peasant.hasGold()){
			heuristic += distToTownHall(peasant.getX(), peasant.getY())+1;
			goldNeeded -= 100;
		}
		
		GoldMine closestMine = closestGold(townhall.getX(), townhall.getY());
		Forest closestForest = closestForest(townhall.getX(), townhall.getY());
		if(goldNeeded > 0)
			heuristic += (2*(distToTownHall(closestMine.getX(), closestMine.getY())) + 1) * (goldNeeded/100);
		if(woodNeeded > 0)
			heuristic += (2*(distToTownHall(closestForest.getX(), closestForest.getY())) + 1) * (woodNeeded/100);
      */
	
		return heuristic;
	}
	
	private int dist(int x1, int y1, int x2, int y2){
		return (Math.abs(x2 - x1) + Math.abs(y2 - y1));
	}
	
	private int distToTownHall(int x, int y){
		return dist(x, y, townhall.getX(), townhall.getY());
	}
}

