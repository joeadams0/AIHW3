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
	
	
	public boolean isGoalState(int GoalGold, int GoalWood){
		return Gold >= GoalGold && Wood >= GoalWood;
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
		return closestGold(p.getX(), p.getY());
	}
	
	public Forest findClosestForest(){
		Peasant p = peasant;
		return closestForest(p.getX(), p.getY());
	}

	public GoldMine closestGold(int x, int y){
		List<GoldMine> mines = GoldMines;
		GoldMine closestMine = null;
		for(GoldMine mine: mines){
			if(closestMine == null){
				if(mine.getGold()>100){
					closestMine = mine;
				}
				else
					continue;
			}
			if((Math.abs(mine.getX() - x)+Math.abs(mine.getX()-y))<(Math.abs(closestMine.getX() - x)+Math.abs(closestMine.getY()-y))){
				closestMine = mine; 
			}
		}
		return closestMine;
	}

	public Forest closestForest(int x, int y){
		List<Forest> forests = Forests;
		Forest closestForest = null;
		for(Forest forest: forests){
			if(closestForest == null){
				if(forest.getWood()>100){
					closestForest = forest;
				}
				else
					continue;
			}
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
	
	public int heuristic(int goalGold, int goalWood){
		int heuristic = 0;
		int goldNeeded = goalGold - Gold;
		int woodNeeded = goalWood - Wood;

		if(goldNeeded == 0 && woodNeeded == 0){
			return heuristic;
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
		heuristic += (2*(distToTownHall(closestMine.getX(), closestMine.getY())) + 1) * (goldNeeded/100);
		heuristic += (2*(distToTownHall(closestForest.getX(), closestForest.getY())) + 1) * (woodNeeded/100);

	
		return heuristic;
	}
	
	private int dist(int x1, int y1, int x2, int y2){
		return Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
	}
	
	private int distToTownHall(int x, int y){
		return dist(x, y, townhall.getX(), townhall.getY());
	}
}

/*
public int heuristic(int goalGold, int goalWood){
	int goldNeeded = goalGold - Gold;
	int woodNeeded = goalWood - Wood;
	int heuristic = 0;
	int nearestGoldmineDist = nearestGoldmineDist();
	int nearestForestDist = nearestForestDist();
	if (peasant.hasGold()){
		heuristic += distanceToTownHall(peasant.getX(), peasant.getY()) + 1;
		goldNeeded -= 100;
	}
	else if(peasant.hasWood()){
		heuristic += distanceToTownHall(peasant.getX(), peasant.getY()) + 1;
		woodNeeded -= 100;
	}
	while(goldNeeded>0){
		goldNeeded -=100;
		heuristic += 2*distance + 2;//travelling back and forth plus harvesting and depositing
		goldMine.setGold(goldMine.getGold() -100);
	}
	while(woodNeeded>0){
		woodNeeded -=100;
		heuristic += 2*distance
		Forest.setWood(Forest.getWood() -100);
	}
	return heuristic;
}
public int nearestGoldmineDist(){
	List<Integer> distances = new ArrayList<Integer>();
	for(GoldMine g : GoldMines){
		if(g.getGold()> 0){
			distances.add(Math.max(Math.abs(townhall.getX()-g.getX()), Math.abs(townhall.getY()-g.getY())));
		}
	}
	return Collections.min(distances);
}
public int nearestForestDist(){
	List<Integer> distances = new ArrayList<Integer>();
	for(Forest f : Forests){
		if(f.getWood()> 0){
			distances.add(Math.max(Math.abs(townhall.getX()-f.getX()), Math.abs(townhall.getY()-f.getY())));
		}
	}
	return Collections.min(distances);
}
*/
