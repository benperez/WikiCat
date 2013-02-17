package walk;
import graph.Category;
import graph.Page;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;



/**
 * Walker (Texas Ranger) is a class which performs a random walk starting from a given node.
 * It then performs a fixed number of steps before terminating at its current node.
 * When this occurs, the node may then be retrieved for further processing.
 */
public class Walker implements Runnable
{
	private final BlockingQueue<Page> queue;
	
	
	/**
	 * Creates a new Walker (Texas Ranger) starting at the given page
	 * @param q The blocking queue this Walker (Texas Ranger) thread will pull pages from
	 */
	public Walker(BlockingQueue<Page> q)
	{
		queue = q;
	}
	
	/**
	 * Runs this particular thread. Keeps pulling pages off of the queue to do walks for.
	 */
	public void run()
	{
		try
		{
			while (true)
			{
				//Grab an item off of the queue, walk a given number of steps, save the results into the database
				Page nextPage = queue.take();
				int nSteps = 3;
				Set<Category> results = walk( nextPage, nSteps );
				saveResults(results);
			}
		} catch(InterruptedException e){ e.printStackTrace(); }
	}
	
	/**
	 * Does a random walk from this Walker (Texas Ranger)s root Page and return the set of categories from the Page it ends up on.
	 * @param steps The number of steps to take from the starting node.
	 * @return A Set of the Categories which the final page is assigned to. 
	 */
	public Set<Category> walk(Page start_page, int steps)
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
	
	/**
	 * Saves the results of this particular walk back to the database
	 * @param results the set of categories that we want to save to the database
	 */
	private void saveResults(Set<Category> results)
	{
		//TODO - implement this Jackson
	}
	
}
