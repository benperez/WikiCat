package walk;

import graph.DBManager;
import graph.Page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Creates a new Walker (Texas Ranger) starting at the given page
 */
public class QueueFiller implements Runnable
{
	private final BlockingQueue<Page> queue;
	
	/**
	 * Creates a new Walker (Texas Ranger) starting at the given page
	 * @param q The blocking queue this Walker (Texas Ranger) thread will pull pages from
	 */
	public QueueFiller(BlockingQueue<Page> q){ queue = q; }
	
	/**
	 * Runs this particular thread, keeps trying to add new pages for processing
	 */
	public void run() {
		try 
		{
			while (true) 
		    { 
				Set<Page> next = queryNextPage();
				///Check if the database is empty, in which case the QueueFiller should sleep for 10 minutes and try again
				if (next==null)
				{
					//Sleep for 10 minutes
					long sleep_time = 1000L * 60L * 10L;
					Thread.sleep(sleep_time);
					continue;
				}
				//Add the pages one at a time
				for (Page p : next)
					queue.put(p); 
		    }
		} catch (InterruptedException ex) { ex.printStackTrace(); }
	}
	
	/**
	 * Queries the database for articles which need to be processed
	 * @return a set of pages to add to the queue for processing
	 */
	private Set<Page> queryNextPage()
	{
		String query = "SELECT page_id from page_todo limit 10;";
		//Get a DB connection
		Connection c = DBManager.getConnection();
		ResultSet rs = DBManager.execute(c, query);
		//Parse result
		Set<Page> pages = new HashSet<Page>();
		try {
			while (rs.next())
			{
				int p_id = rs.getInt("page_id");
				pages.add( new Page(p_id));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		//Release the connection back to the pool.
		DBManager.closeConnection(c, rs);
		//Return null if result set is empty
		if (pages.isEmpty())
			return null;
		
		//Remove the entries just received from the table
		StringBuffer delete_query = new StringBuffer();
		delete_query.append("DELETE FROM page_todo where page_id = ");
		boolean first = true;
		for (Page p : pages)
		{
			if (first)
			{
				first = false;
				delete_query.append(p.pageId);
			}
			else
			{
				delete_query.append(" OR page_id = "+p.pageId);
			}
		}
		delete_query.append(";");
		c = DBManager.getConnection();
		rs = DBManager.execute(c, delete_query.toString(), true);
		DBManager.closeConnection(c, rs);
		
		return pages;
	}
	
}