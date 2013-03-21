package Assignment3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;


public class Planner {
	public static StateNode plan(State state, List<StripsAction> actions, int goalGold, int goalWood){
		// Create openlist
		PriorityQueue<StateNode> openList = new PriorityQueue<StateNode>();
		
		// Create ClosedList
		List<StateNode> closedList = new ArrayList<StateNode>();
		
		StateNode root = new StateNode(state, null, null, 0, goalGold, goalWood);
		openList.add(root);
		
		// Search for the path
		return search(openList, closedList, actions, goalGold, goalWood);
	}
	private static StateNode search(PriorityQueue<StateNode> openList, List<StateNode> closedList, List<StripsAction> actions, int goalGold, int goalWood){
		
		while(openList.size()>0){
			
			StateNode head = openList.peek();
			openList.remove(head);
			/*System.out.println("\nExpanding Node...");
			System.out.println(head);
			System.out.println("\nPrinting Open List: " + openList.size());
			printOpenList(openList);*/
			// If it is the goal state
			if(head.reachesGoal(goalGold, goalWood)){
				// Get path back to root
				return searchComplete(head);
			}
			
			// Expand Node
			else{
				List<StateNode> neighbors = possibleMoves(head, actions, goalGold, goalWood);
				StateNode neighbor = null;
				for(int i = 0; i < neighbors.size(); i++){
					neighbor = neighbors.get(i);
					// If it is not in the closed list
					if(!(closedList.contains(neighbor))){
						// If it is already on the open list
						if(openList.contains(neighbor)){
							updateNode(openList, neighbor);
						}
						// Add it to the open List
						else {
							openList.add(neighbor);
						}
					}
				}
				closedList.add(head);
			}
		}
		System.out.println("No solution found!");
		return null;
	}
	//If the node is already in the openlist, it updates the cost if it is shorter. 
	private static void updateNode(PriorityQueue<StateNode> openList, StateNode neighbor){
		Iterator<StateNode> itr = openList.iterator();
		while(itr.hasNext()){
			StateNode temp = itr.next();
			// Same location
			if(temp.equals(neighbor)){
				// new node has a shorter path
				if(temp.getCost() > neighbor.getCost()){
					itr.remove();
					openList.add(neighbor);
					break;
				}
			}
		}
	}
	private static List<StateNode> possibleMoves(StateNode head, List<StripsAction> actions, int goalGold, int goalWood){
		List<StateNode> possibleNextStates = new ArrayList<StateNode>();
		for(StripsAction a : actions){
			if(a.precondition(head.getState())){
				StateNode tmp = new StateNode(a.postcondition(head.getState()), head, a, head.getCost() + a.getMakespan(head.getState()), goalGold, goalWood);
				possibleNextStates.add(tmp);
			}
		}
		return possibleNextStates;
	}
	// Retraces path back to root
	private static StateNode searchComplete(StateNode goal){
		StateNode temp = goal;
		StateNode tempParent = null;
		while(temp.getParent() != null){
			tempParent = temp.getParent();
			// Set the child link
			tempParent.setChild(temp);
			tempParent.setNextAction(temp.getParentAction());
			temp = tempParent;
		}
		return temp;
	}

	private static void printOpenList(PriorityQueue<StateNode> openList){
		StateNode head = openList.peek();
		if(head == null){
			return;
		}
		System.out.println(head);
		openList.remove(head);
		printOpenList(openList);
		openList.add(head);
	}
}
