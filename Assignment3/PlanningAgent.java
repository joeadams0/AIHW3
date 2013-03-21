package Assignment3;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.io.*;

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
	Map<List<Integer>, StripsAction> busyPeasantManager;

	@Override
	public Map<Integer, Action> initialStep(StateView newstate, History.HistoryView statehistory) {
		step = 0;
		currentState = newstate;
		busyPeasantManager = new HashMap<List<Integer>, StripsAction>();
		
		currentNode = plan();
		StateNode root = currentNode;
		try{
			PrintWriter out = new PrintWriter(new FileWriter("plan.txt")); 
			if(root!= null){
				while(root.getChild() != null){
					out.println(root.getNextAction());
					root = root.getChild();
				}
			}
			out.close();
		}
		catch(IOException ex){
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

		checkBusyPeasants();
		List<UnitView> idlePeasants = getIdlePeasants();
		State state = null;
		// Perform next action
		while(idlePeasants.size()>0){
			state = generateState(generatePeasants(idlePeasants));
			StripsAction action = currentNode.getNextAction();

			if(action.precondition(state)){
				List<Action> actions = generateActions(eligablePeasants(idlePeasants, action), action);
				List<Integer> busyPeasants = new ArrayList<Integer>();
				for(Action a : actions){
					builder.put(a.getUnitId(), a);
					busyPeasants.add(a.getUnitId());
				}
				busyPeasantManager.put(busyPeasants, action);
				currentNode = currentNode.getChild();
				idlePeasants = getIdlePeasants();
			}
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
		List<UnitView> idlePeasants = getIdlePeasants();
		State state = generateState(generatePeasants(idlePeasants));
		return Planner.plan(state, generateAllActions(state), goldRequired, woodRequired);
	}
	
	private List<StripsAction> generateAllActions(State state){
		List<StripsAction> actions = new ArrayList<StripsAction>();
		List<UnitView> p = getPeasants();
		
		DepositAction deposit = new DepositAction();
		HarvestGoldAction harvestGold = new HarvestGoldAction();
		HarvestWoodAction harvestWood = new HarvestWoodAction();
		DepositAction2 deposit2 = new DepositAction2();
		HarvestGoldAction2 harvestGold2 = new HarvestGoldAction2();
		HarvestWoodAction2 harvestWood2 = new HarvestWoodAction2();
		DepositAction3 deposit3 = new DepositAction3();
		HarvestGoldAction3 harvestGold3 = new HarvestGoldAction3();
		HarvestWoodAction3 harvestWood3 = new HarvestWoodAction3();
		
		actions.add(deposit);
		actions.add(harvestGold);
		actions.add(harvestWood); 
		actions.add(deposit2);
		actions.add(harvestGold2);
		actions.add(harvestWood2);
		actions.add(deposit3);
		actions.add(harvestGold3);
		actions.add(harvestWood3);
		
		List<StripsLocation> locations = generateMoveLocations();
		for(StripsLocation loc : locations){
			actions.add(new MoveAction(loc));
			actions.add(new MoveAction2(loc));
			actions.add(new MoveAction3(loc));
		}
		
		return actions;	
	}
	
	private List<StripsLocation> generateMoveLocations(){
		List<StripsLocation> locations = new ArrayList<StripsLocation>();
		List<ResourceView> resources = currentState.getAllResourceNodes();
		List<UnitView> units = currentState.getUnits(playernum);
		for(ResourceView resource : resources){
			if(resource.getType() == Type.TREE){
				locations.add(generateForest(resource));
			}
			else
				locations.add(generateGoldMine(resource));
		}
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("TownHall")){
				locations.add(generateTownhall(unit));
			}
		}
		return locations;
	}
	
	private State generateState(List<Peasant> p){
		Townhall t = null;
		List<GoldMine> mines = new ArrayList<GoldMine>();
		List<Forest> forests = new ArrayList<Forest>();
		List<ResourceView> resources = currentState.getAllResourceNodes();
		List<UnitView> units = currentState.getUnits(playernum);
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("TownHall")){
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

	private List<Peasant> generatePeasants(List<UnitView> units){
		List<Peasant> peasants = new ArrayList<Peasant>();
		for(UnitView unit : units){
			peasants.add(generatePeasant(unit));
		}
		return peasants;
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

	private List<Action> generateActions(List<UnitView> idlePeasants, StripsAction action){
		List<Action> actions = new ArrayList<Action>();

		if(action instanceof MoveAction){
			StripsLocation loc = ((MoveAction)action).getLocation();
			//System.out.println("Performing Move to " + loc);
			actions.add(generateMoveAction(idlePeasants.get(0), loc));
		}
		else if(action instanceof HarvestGoldAction){
			//System.out.println("Performing Harvest Gold");
			UnitView p = idlePeasants.get(0);
			ResourceView mine = findNearestMine(p.getXPosition(), p.getYPosition());
			actions.add(generateGatherAction(p, mine));
		}
		else if(action instanceof HarvestWoodAction){
			//System.out.println("Performing Harvest Wood");
			UnitView p = idlePeasants.get(0);
			ResourceView forest = findNearestForest(p.getXPosition(), p.getYPosition());
			actions.add(generateGatherAction(p, forest));
		}
		else if(action instanceof DepositAction){
			//System.out.println("Performing Deposit");
			UnitView townhall = getTownhall();
			actions.add(generateDepositAction(idlePeasants.get(0), townhall));
		}
		else if(action instanceof MoveAction2){
			StripsLocation loc = ((MoveAction)action).getLocation();
			//System.out.println("Performing Move2 to: " + loc);
			actions.add(generateMoveAction(idlePeasants.get(0), loc));
			actions.add(generateMoveAction(idlePeasants.get(1), loc));
		}
		else if(action instanceof HarvestGoldAction2){
		//	System.out.println("Performing Harvest Gold2");
			UnitView p1 = idlePeasants.get(0);
			UnitView p2 = idlePeasants.get(1);
			ResourceView mine1 = findNearestMine(p1.getXPosition(), p1.getYPosition());
			ResourceView mine2 = findNearestMine(p2.getXPosition(), p2.getYPosition());
			actions.add(generateGatherAction(p1, mine1));
			actions.add(generateGatherAction(p2, mine2));
		}
		else if(action instanceof HarvestWoodAction2){
			//System.out.println("Performing Harvest Wood2");
			UnitView p1 = idlePeasants.get(0);
			UnitView p2 = idlePeasants.get(1);
			ResourceView forest1 = findNearestForest(p1.getXPosition(), p1.getYPosition());
			ResourceView forest2 = findNearestForest(p2.getXPosition(), p2.getYPosition());
			actions.add(generateGatherAction(p1, forest1));
			actions.add(generateGatherAction(p2, forest2));
		}
		else if(action instanceof DepositAction2){
			//System.out.println("Performing Deposit2");
			UnitView townhall = getTownhall();
			actions.add(generateDepositAction(idlePeasants.get(0), townhall));
			actions.add(generateDepositAction(idlePeasants.get(1), townhall));
		}
		else if(action instanceof MoveAction3){
			StripsLocation loc = ((MoveAction)action).getLocation();
			//System.out.println("Performing Move3 to: " + loc);
			actions.add(generateMoveAction(idlePeasants.get(0), loc));
			actions.add(generateMoveAction(idlePeasants.get(1), loc));
			actions.add(generateMoveAction(idlePeasants.get(2), loc));
		}
		else if(action instanceof HarvestGoldAction3){
			//System.out.println("Performing Harvest Gold3");
			UnitView p1 = idlePeasants.get(0);
			UnitView p2 = idlePeasants.get(1);
			UnitView p3 = idlePeasants.get(2);
			ResourceView mine1 = findNearestMine(p1.getXPosition(), p1.getYPosition());
			ResourceView mine2 = findNearestMine(p2.getXPosition(), p2.getYPosition());
			ResourceView mine3 = findNearestMine(p3.getXPosition(), p3.getYPosition());
			actions.add(generateGatherAction(p1, mine1));
			actions.add(generateGatherAction(p2, mine2));
			actions.add(generateGatherAction(p3, mine3));
		}
		else if(action instanceof HarvestWoodAction3){
			//System.out.println("Performing Harvest Wood3");
			UnitView p1 = idlePeasants.get(0);
			UnitView p2 = idlePeasants.get(1);
			UnitView p3 = idlePeasants.get(2);
			ResourceView forest1 = findNearestForest(p1.getXPosition(), p1.getYPosition());
			ResourceView forest2 = findNearestForest(p2.getXPosition(), p2.getYPosition());
			ResourceView forest3 = findNearestForest(p3.getXPosition(), p3.getYPosition());
			actions.add(generateGatherAction(p1, forest1));
			actions.add(generateGatherAction(p2, forest2));
			actions.add(generateGatherAction(p3, forest3));
		}
		else if(action instanceof DepositAction3){
			//System.out.println("Performing Deposit3");
			UnitView townhall = getTownhall();
			actions.add(generateDepositAction(idlePeasants.get(0), townhall));
			actions.add(generateDepositAction(idlePeasants.get(1), townhall));
			actions.add(generateDepositAction(idlePeasants.get(2), townhall));
		}
		return actions;
	}
	
	private Action generateMoveAction(UnitView peasant, StripsLocation loc){
		return Action.createCompoundMove(peasant.getID(), loc.getX(), loc.getY());
	}
	
	private Action generateGatherAction(UnitView peasant, ResourceView resource){
		return Action.createCompoundGather(peasant.getID(), resource.getID());
	}
	
	private Action generateDepositAction(UnitView peasant, UnitView townhall){
		return Action.createCompoundDeposit(peasant.getID(), townhall.getID());
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

	private List<UnitView> getPeasants(){
		List<UnitView> units = currentState.getUnits(playernum);
		List<UnitView> peasants = new ArrayList<UnitView>();
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("Peasant")){
				peasants.add(unit);
			}
		}
		return peasants;
	}

	private List<UnitView> getIdlePeasants(){
		List<UnitView> freePeasants = getPeasants();
		for(List<Integer> bPeasants : busyPeasantManager.keySet()){
			for(Integer i : bPeasants){
				Iterator<UnitView> itr = freePeasants.iterator();
				while(itr.hasNext()){
					if(i.intValue() == itr.next().getID()){
						itr.remove();
					}
				}
			}
		}
		return freePeasants;
	}

	private List<UnitView> eligablePeasants(List<UnitView> units, StripsAction action){
		List<Peasant> peasants = action.findEligablePeasants(generateState(generatePeasants(units)));
		List<UnitView> eligablePeasants = new ArrayList<UnitView>();
		for(Peasant p : peasants){
			for(UnitView unit : units){
				if(unit.getXPosition() == p.getX() && unit.getYPosition() == p.getY()){
					eligablePeasants.add(unit);
				}
			}
		}
		return eligablePeasants;
	}
	
	private void checkBusyPeasants(){
		Iterator<List<Integer>> itr = busyPeasantManager.keySet().iterator();
		List<Integer> key;
		State state;
		while(itr.hasNext()){
			key = itr.next();
			List<UnitView> busyPeasants = new ArrayList<UnitView>();
			for(Integer i : key){
				busyPeasants.add(currentState.getUnit(i.intValue()));
			}
			state = generateState(generatePeasants(busyPeasants));
			StripsAction action = busyPeasantManager.get(key);
			if(!action.precondition(state)){
				busyPeasantManager.remove(key);
			}
		}
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
