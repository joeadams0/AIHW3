package Assignment3;
import java.util.Comparator;
public class TotalCostComparator implements Comparator<StateNode> {

	@Override
	public int compare(StateNode x, StateNode y)
	{
	    // Assume neither string is null. Real code should
        // probably be more robust
        if (x.totalEstimatedCost() < y.totalEstimatedCost())
        {
            return -1;
        }
        if (x.totalEstimatedCost() > y.totalEstimatedCost())
        {
            return 1;
        }
        return 0;
    }
}
