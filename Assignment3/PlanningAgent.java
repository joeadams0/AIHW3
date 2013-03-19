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
	
	@Override
	public Map<Integer, Action> initialStep(StateView newstate, History.HistoryView statehistory) {
		step = 0;
		currentState = newstate;
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
		
		StateNode root = Planner.plan(state, actions, goldRequired, woodRequired);
		
		return middleStep(newstate, statehistory);
	}

	@Override
	public Map<Integer,Action> middleStep(StateView newState, History.HistoryView statehistory) {
		step++;
		Map<Integer,Action> builder = new HashMap<Integer,Action>();
		currentState = newState;
		return builder;
	}

	@Override
	public void terminalStep(StateView newstate, History.HistoryView statehistory) {
		step++;
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
	
	private State generateState(){
		Peasant p = null;
		Townhall t = null;
		List<GoldMine> mines = new ArrayList<GoldMine>();
		List<Forest> forests = new ArrayList<Forest>();
		List<ResourceView> resources = currentState.getAllResourceNodes();
		List<UnitView> units = currentState.getUnits(playernum);
		for(UnitView unit : units){
			String unitTypeName = unit.getTemplateView().getName();
			if(unitTypeName.equals("Peasant")){
				p = generatePeasant(unit);
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
		return new Peasant(unit.getXPosition(), unit.getYPosition());
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
	
	private int getCurrentGold(){
		return currentState.getResourceAmount(playernum, ResourceType.GOLD);
	}
	
	private int getCurrentWood(){
		return currentState.getResourceAmount(playernum, ResourceType.WOOD);
	}
}
