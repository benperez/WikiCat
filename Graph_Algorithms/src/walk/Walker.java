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
=======
package walk;

import graph.Category;
import graph.DBManager;
import graph.Page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;



/**
 * Walker (Texas Ranger) is a class which generates category suggestions for articles.
 */
public class Walker implements Runnable
{
	private final BlockingQueue<Page> queue;
	private final int N_SAMPLES;
	
	/**
	 * Creates a new Walker (Texas Ranger) thread
	 * @param q The blocking queue this Walker (Texas Ranger) thread will pull pages from
	 */
	public Walker(BlockingQueue<Page> q, int n)
	{
		queue = q;
		N_SAMPLES = n;
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
				//Save the results
				saveResults(nextPage, results);
			}
		} catch(InterruptedException e){ e.printStackTrace(); }
	}
	
	/**
	 * Generates Category suggestions for a given page
	 * @param p the page to generate suggestions for
	 * @return a mapping of Category->frequency of occurrence, its score
	 */
	private Map<Category,Double> handle(Page p)
	{
		//Get all the categories that are currently on this page
		Set<Category> root_categories = p.getCategories();
		Map<Category, Integer> counts = new HashMap<Category,Integer>();
		//Try and do at least N_SAMPLES sample per category of the root page
		for (Category cat : root_categories)
		{
			int n_sampled = 0;
			Set<Page> c_pages = cat.getPages();
			for (Page c_p : c_pages)
			{
				if (n_sampled++ > N_SAMPLES)
					break;
				Set<Category> c_p_categories = c_p.getCategories();
				//Add the categories of each sample to the overall category counts
				for (Category c_p_cat : c_p_categories)
				{
					if (counts.containsKey(c_p_cat))
						counts.put(c_p_cat, counts.get(c_p_cat)+1);
					else
						counts.put(c_p_cat, 1);
				}
			}
		}
		//Turn counts into overall frequencies
		double n_categories = 0.0;
		for (Category c : counts.keySet())
		{
			n_categories+=counts.get(c);
		}
		Map<Category, Double> frequencies = new HashMap<Category, Double>();
		Iterator<Entry<Category,Integer>> it = counts.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<Category,Integer> pairs = it.next();
	        frequencies.put(pairs.getKey(), pairs.getValue() / n_categories);
		}
		return frequencies;
	}
	
	/**
	 * Saves the results of this particular walk back to the database
	 * @param p the page associated with these category suggestions
	 * @param results the set of categories that we want to save to the database
	 */
	private void saveResults(Page p, Map<Category,Double> results)
	{
		//Generate an insert query for all the results
		String query = "INSERT INTO page_results (pageid, category, score)\nVALUES\n";
		int n_results = results.size();
		int result_i = 0;
		Iterator<Entry<Category,Double>> it = results.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<Category,Double> pairs = it.next();
			query+="( "+p.pageId+", '"+pairs.getKey().getName()+"', "+pairs.getValue()+")";
			if (result_i++<n_results-1)
				query+=",\n";
			else
				query+=";";
		}
		//Send the query to the database
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
	}
	
}