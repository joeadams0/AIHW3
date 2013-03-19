
public class StateNode {
	private State state;
	private StateNode child = null;
	private StateNode parent = null;
	private StripsAction parentAction=null;
	private StripsAction nextAction=null;
	private int cost;
	public StateNode(State state, StateNode parentNode, StripsAction parentAction, int cost){
		this.state = state;
		this.parent = parentNode;
		this.cost = cost;
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
		return (cost + state.heuristic());
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
		return( goalGold <= state.getGold() && goalWood <= state.getWood());
	}
}

