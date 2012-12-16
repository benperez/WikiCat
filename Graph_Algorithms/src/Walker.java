import java.util.Map;
import java.util.Set;



/**
 * Walker (Texas Ranger) is a class which performs a random walk starting from a given node.
 * It then performs a fixed number of steps before terminating at its current node.
 * When this occurs, the node may then be retrieved for further processing.
 */
public class Walker
{
	
	private Page start_page;
	
	
	/**
	 * Creates a new Walker (Texas Ranger) starting at the given page
	 * @param start The page this Walker (Texas Ranger) will start from when walking
	 */
	public Walker(Page start)
	{
		start_page = start;
	}
	
	
	/**
	 * Does a random walk from this Walker (Texas Ranger)s root Page and return the set of categories from the Page it ends up on.
	 * @param steps The number of steps to take from the starting node.
	 * @return A Set of the Categories which the final page is assigned to. 
	 */
	public Set<Category> walk(int steps)
	{
		//Do the walk for the desired number of steps
		Page current_page = start_page;
		for (int i=steps; i>0; i--)
		{
			//Choose where to go based on the transition probabilities for the current Page
			Map<Page,Double> transitions = current_page.getTransitionProbabilites();
			double rand = Math.random(); //Choose a random number btwn 0 and 1
			double range_start = 0.0;
			for (Page p : transitions.keySet())
			{
				//Map each transition probability to a range between [0-1] and see which one our random choice falls within.
				double range_end = range_start + transitions.get(p);
				if (range_start<rand && rand<range_end)
				{
					current_page = p;
					break;
				}
				//Move to the next range
				range_start = range_end;
			}
		}
		
		return current_page.getCategories();
	}
	
}
