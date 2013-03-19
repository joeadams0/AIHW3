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
}