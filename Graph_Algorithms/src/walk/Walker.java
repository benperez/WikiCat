package walk;

import graph.Category;
import graph.Page;

import java.util.HashMap;
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
	 * Runs this particular thread. Pulls a page off the queue, does work on it, saves the results
	 */
	public void run()
	{
		try
		{
			while (true)
			{
				//Grab an item off of the queue
				Page nextPage = queue.take();
				//Get category suggestions
				Map<Category, Double> results = handle(nextPage);
				//TODO - save the results
				saveResults(nextPage, results);
			}
		} catch(InterruptedException e){ e.printStackTrace(); }
	}
	
	private Map<Category,Double> handle(Page p)
	{
		//TODO - implement this
		int N_SAMPLES = 1;
		
		//Get all the categories that are currently on this page
		Set<Category> categories = p.getCategories();
		Map<Category, Integer> counts = new HashMap<Category,Integer>();
		for (Category c : categories)
		{
			//Do at least one sample per category
			
		}
		return null;
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
	 * @param p the page associated with these category suggestions
	 * @param results the set of categories that we want to save to the database
	 */
	private void saveResults(Page p, Map<Category,Double> results)
	{
		String insert_query = "GO\nINSERT INTO page_results (pageid, category, score) ";
		int n_results = results.size();
		int n_seen = 0;
		for (Category c : results.keySet())
		{
			insert_query+="SELECT "+p.pageId+", "+c.getName()+", "+results.get(c)+"\n";
			if (n_seen<n_results-2)
				insert_query+="UNION ALL\n";
			n_seen++;
		}
		//TODO - actually send this query
	}
	
}
