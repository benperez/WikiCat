package walk;

import graph.Category;
import graph.DBManager;
import graph.Page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringEscapeUtils;



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
				//Delete from page_todo
				String delete_query = "DELETE FROM page_todo where page_id = "+nextPage.pageId+";";
				Connection c = DBManager.getConnection();
				ResultSet rs = DBManager.execute(c, delete_query, true);
				DBManager.closeConnection(c, rs);
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
		if (results.size()==0)
			return;
		//Generate an insert query for all the results
		String query = "INSERT INTO page_results (page_id, cat_name, score) VALUES ";
		int n_results = 10 > results.size() ? results.size() : 10;
		///Sort the results
		List<Map.Entry<Category,Double>> sortedByScore = new ArrayList<Map.Entry<Category,Double>>(results.entrySet()); 
		Collections.sort(sortedByScore, new Comparator<Map.Entry<Category,Double>>() {
			public int compare(Map.Entry<Category,Double> c1, Map.Entry<Category,Double> c2)
			{
				double score1 = c1.getValue();
				double score2 = c2.getValue();
				if (score1 > score2)
					return 1;
				else if (score1 == score2)
					return 0;
				else
					return -1;
			}
		});
		
		//Build the query of top N results
		int result_i = 0;
		for ( int i=0; i<n_results; i++)
		{
			Entry<Category,Double> pairs = sortedByScore.get(i);
			query+="( "+p.pageId+", '"+pairs.getKey().getName()+"', "+pairs.getValue()+")";
			if (result_i++<n_results-1)
				query+=",";
			else
				query+=";";
		}
		//Send the query to the database
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query, true);
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
	}
	
}