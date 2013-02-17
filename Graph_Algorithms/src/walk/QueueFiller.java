package walk;

import graph.Page;

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
				Page next = queryNextPage();
				///Check if the database is empty, in which case the QueueFiller should exit
				if (next==null){ return; }
				queue.put(next); 
		    }
		} catch (InterruptedException ex) { ex.printStackTrace(); }
	}
	
	/**
	 * Queries the database for articles which need to be processed
	 * @return a page to add to the queue for processing
	 */
	private Page queryNextPage()
	{
		///TODO - implement this...
		return new Page(0);
	}
	
}
