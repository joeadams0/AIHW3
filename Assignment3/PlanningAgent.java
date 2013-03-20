package Assignment3;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.environment.model.history.History;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.ResourceNode.Type;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Template.TemplateView;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;
import edu.cwru.sepia.experiment.Configuration;
import edu.cwru.sepia.experiment.ConfigurationValues;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.model.state.ResourceNode.Type;
import edu.cwru.sepia.environment.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.util.Direction;

/**
 * This agent will first collect gold to produce a peasant,
 * then the two peasants will collect gold and wood separately until reach goal.
 * @author Feng
 *
 */
public class PlanningAgent extends Agent {
	private static final long serialVersionUID = -4047208702628325380L;
	private static final Logger logger = Logger.getLogger(PlanningAgent.class.getCanonicalName());

	private int goldRequired;
	private int woodRequired;
	
	private int step;
	
	public PlanningAgent(int playernum, String[] arguments) {
		super(playernum);
		
		goldRequired = Integer.parseInt(arguments[0]);
		woodRequired = Integer.parseInt(arguments[1]);
	}

	StateView currentState;
	StateNode currentNode;

	@Override
	public Map<Integer, Action> initialStep(StateView newstate, History.HistoryView statehistory) {
		step = 0;
		currentState = newstate;

		currentNode = plan();

		StateNode root = currentNode;
		if(root!= null){
			while(root.getChild() != null){
				System.out.println(root.getNextAction());
				root = root.getChild();
			}
		}
		return middleStep(newstate, statehistory);
	}

	@Override
	public Map<Integer,Action> middleStep(StateView newState, History.HistoryView statehistory) {
		step++;
		Map<Integer,Action> builder = new HashMap<Integer,Action>();
		currentState = newState;
		if(currentNode == null){
			return builder;
		}
		if(currentNode.getNextAction() == null){
			return builder;
		}

		UnitView p = getPeasant();
		State state = generateState();
		//System.out.println("CurrentNode:\n" + currentNode.getState() + "\nCurrent State:\n" + state + "\nPrecondition:\n"+ currentNode.getNextAction().precondition(state));
		// Perform next action
		if(currentNode.getNextAction().precondition(state)){
			StripsAction action = currentNode.getNextAction();
			builder.put(p.getID(), generateAction(p, action));
			currentNode = currentNode.getChild();
		}
		return builder;
	}

	@Override
	public void terminalStep(StateView newstate, History.HistoryView statehistory) {
		step++;
		if(logger.isLoggable(Level.FINE))
		{
			logger.fine("=> Step: " + step);
		}
		
		int currentGold = newstate.getResourceAmount(0, ResourceType.GOLD);
		int currentWood = newstate.getResourceAmount(0, ResourceType.WOOD);
		
		if(logger.isLoggable(Level.FINE))
		{
			logger.fine("Current Gold: " + currentGold);
		}
		if(logger.isLoggable(Level.FINE))
		{
			logger.fine("Current Wood: " + currentWood);
		}
		if(logger.isLoggable(Level.FINE))
		{
			logger.fine("Congratulations! You have finished the task!");
		}
		System.out.println("=> Step: " + step);
		System.out.println("Current Gold: " + currentGold);
		System.out.println("Current Wood: " + currentWood);
		System.out.println("Congratulations! You have finished the task!");
	}

	public static String getUsage() {
		return "Two arguments, amount of gold to gather and amount of wood to gather";
	}
	@Override
	public void savePlayerData(OutputStream os) {
		//this agent lacks learning and so has nothing to persist.
		
	}
	@Override
	public void loadPlayerData(InputStream is) {
		//this agent lacks learning and so has nothing to persist.
	}
	
	private StateNode plan(){
		List<StripsAction> actions = new ArrayList<StripsAction>();
		
		State state = generateState();
		
		DepositAction deposit = new DepositAction();
		HarvestGoldAction harvestGold = new HarvestGoldAction();
		HarvestWoodAction harvestWood = new HarvestWoodAction();
		MoveToGoldAction moveToGold = new MoveToGoldAction();
		MoveToWoodAction moveToWood = new MoveToWoodAction();
		MoveToTownhallAction moveToTownhall = new MoveToTownhallAction();
		
		actions.add(deposit);
		actions.add(harvestGold);
		actions.add(harvestWood);
		actions.add(moveToGold);
		actions.add(moveToWood);
		actions.add(moveToTownhall);

		return Planner.plan(state, actions, goldRequired, woodRequired);
	}

	private State generateState(){
		List<Peasant> p = null;
		Townhall t = null;
		List<GoldMine> mines = new ArrayList<GoldMine>();
		List<Forest> forests = new ArrayList<Forest>();
		List<ResourceView> resources = currentState.getAllResourceNodes();
		List<UnitView> units = currentState.getUnits(playernum);
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("Peasant")){
				p.add(generatePeasant(unit));
			}
			else if(unitTypeName.equals("TownHall")){
				t = generateTownhall(unit);
			}
		}
		
		for(ResourceView resource : resources){
			if(resource.getType() == Type.TREE){
				forests.add(generateForest(resource));
			}
			else
				mines.add(generateGoldMine(resource));
		}
		
		return new State(mines, forests, getCurrentGold(), getCurrentWood(), p, t);
	}
	
	private Peasant generatePeasant(UnitView unit){
		Peasant p = new Peasant(unit.getXPosition(), unit.getYPosition());
		if(unit.getCargoAmount()>0){
			if(unit.getCargoType() == ResourceType.GOLD){
				p.setGold(true);
			}
			else{
				p.setWood(true);
			}
		}
		return p; 
	}
	
	private Townhall generateTownhall(UnitView unit){
		return new Townhall(unit.getXPosition(), unit.getYPosition());
	}
	
	private GoldMine generateGoldMine(ResourceView resource){
		return new GoldMine(resource.getXPosition(), resource.getYPosition(), resource.getAmountRemaining());
	}
	
	private Forest generateForest(ResourceView resource){
		return new Forest(resource.getXPosition(), resource.getYPosition(), resource.getAmountRemaining());
	}

	private Action generateAction(UnitView peasant, StripsAction action){
		if(action == null){
			System.out.println("Error: Cannot find solution");
			return null;
		}
		if(action instanceof MoveToTownhallAction){
			System.out.println("Performing Move to Townhall");
			UnitView townhall = getTownhall();
			return Action.createCompoundMove(peasant.getID(), townhall.getXPosition(), townhall.getYPosition());
		}
		else if(action instanceof MoveToGoldAction){
			System.out.println("Performing Move to GoldMine");
			ResourceView mine = findNearestMine(peasant.getXPosition(), peasant.getYPosition());
			return Action.createCompoundMove(peasant.getID(), mine.getXPosition(), mine.getYPosition());
		}
		else if(action instanceof MoveToWoodAction){
			System.out.println("Performing Move to Forest");
			ResourceView forest = findNearestForest(peasant.getXPosition(), peasant.getYPosition());
			return Action.createCompoundMove(peasant.getID(), forest.getXPosition(), forest.getYPosition());
		}
		else if(action instanceof HarvestGoldAction){
			System.out.println("Performing Harvest Gold");
			ResourceView mine = findNearestMine(peasant.getXPosition(), peasant.getYPosition());
			return Action.createCompoundGather(peasant.getID(), mine.getID());
		}
		else if(action instanceof HarvestWoodAction){
			System.out.println("Performing Harvest Wood");
			ResourceView forest = findNearestForest(peasant.getXPosition(), peasant.getYPosition());
			return Action.createCompoundGather(peasant.getID(), forest.getID());
		}
		else if(action instanceof DepositAction){
			System.out.println("Performing Deposit");
			UnitView townhall = getTownhall();
			return Action.createCompoundDeposit(peasant.getID(), townhall.getID());
		}
		else{
			return null;
		}
	}

	private ResourceView findNearestResource(int x, int y, Type type){
		List<ResourceView> resources = currentState.getAllResourceNodes();
		ResourceView nearestResource = resources.get(0);
		for(ResourceView resource: resources){
			if(resource.getType() == type){
				if(distance(resource.getXPosition(), resource.getYPosition(), x, y) < distance(nearestResource.getXPosition(), nearestResource.getYPosition(), x, y)){
					nearestResource = resource;
				}
			}
		}
		return nearestResource;
	}

	private ResourceView findNearestMine(int x, int y){
		return findNearestResource(x, y, Type.GOLD_MINE);
	}

	private ResourceView findNearestForest(int x, int y){
		return findNearestResource(x, y, Type.TREE);
	}

	private UnitView getPeasant(){
		List<UnitView> units = currentState.getUnits(playernum);
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("Peasant")){
				return unit;
			}
		}
		return null;
	}

	private UnitView getTownhall(){
		List<UnitView> units = currentState.getUnits(playernum);
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("TownHall")){
				return unit;
			}
		}
		return null;
	}

	private int distance(int x1, int y1, int x2, int y2){
		return (Math.abs(x1 - x2) + Math.abs(y1 - y2));
	}
	
	private int getCurrentGold(){
		return currentState.getResourceAmount(playernum, ResourceType.GOLD);
	}
	
	private int getCurrentWood(){
		return currentState.getResourceAmount(playernum, ResourceType.WOOD);
	}
}
