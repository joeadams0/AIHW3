package Assignment3;
import java.lang.Comparable;
public class StateNode implements Comparable<StateNode> {
	private State state;
	private StateNode child = null;
	private StateNode parent = null;
	private StripsAction parentAction=null;
	private StripsAction nextAction=null;
	private int cost;
	private int goalGold;
	private int goalWood;

	public StateNode(State state, StateNode parentNode, StripsAction parentAction, int cost, int goalGold, int goalWood){
		this.state = state;
		this.parent = parentNode;
		this.parentAction = parentAction;
		this.cost = cost;
		this.goalGold = goalGold;
		this.goalWood = goalWood;
	}
	public void setChild(StateNode childNode){
		this.child = childNode;
	}
	public StateNode getChild(){
		return child;
	}
	public StateNode getParent(){
		return parent;
	}
	public State getState(){
		return state;
	}
	public int getCost(){
		return cost;
	}
	public int totalEstimatedCost(){
		return (cost + state.heuristic(goalGold, goalWood));
	}
	public StripsAction getParentAction(){
		return parentAction;
	}
	public StripsAction getNextAction(){
		return nextAction;
	}
	public void setNextAction(StripsAction a){
		nextAction = a;
	}
	public boolean equals(Object obj){
		if(obj.getClass() != this.getClass()){
			return false;
		}
		StateNode node = (StateNode)obj;
		return ( node.getState().equals(this.state) );
	}
	public boolean reachesGoal(int goalGold, int goalWood){
		return state.isGoalState(goalGold, goalWood);
	}

	@Override
	public int compareTo(StateNode y)
	{
	    // Assume neither string is null. Real code should
        // probably be more robust
        if (this.totalEstimatedCost() < y.totalEstimatedCost())
        {
            return -1;
        }
        if (this.totalEstimatedCost() > y.totalEstimatedCost())
        {
            return 1;
        }
        return 0;
    }

    public String toString(){
    	return "StateNode:\n" + parentAction + "\n"+ state + "\nCost: " + totalEstimatedCost() +"\n";
    }
}

