package graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Page Manager, links the SQL database to our java framework
 * 
 * Works by lazy loading pages from the database only when requested. Additionally, pages are cached and never loaded a second time.
 */
public class PageManager 
{
	//The cache of already-loaded pages
	private static Map<Integer,Page> loadedPages;
	
	
	//Static initializer for the cached pages
	static 
	{
		//Instantiate the empty set of pages
		loadedPages = new HashMap<Integer,Page>();
	}
	
	
	/**
	 * Retrieves a set of all pages cross-linked from the provided page.
	 * @param page The Page to get outgoing neighbors for.
	 * @return A set of pages linked to or null if an error occurred.
	 */
	public static Set<Page> getOutgoingLinks(Page page)
	{
		//Run a query to get the page ids linked to by the given one
		//TODO Do some more sanity checking to make sure we're getting good pages
		String query =  "SELECT P.page_id as id " +
						"FROM pagelinks as PL, page as P " +
						"WHERE P.page_namespace=0 " +
						"AND PL.pl_from="+page.pageId+" AND PL.pl_title=P.page_title;";
		ResultSet rs = DBManager.query(query);
		if (rs==null){
			return null;
		}
		
		//Populate the return set of pages
		Set<Page> pages = new HashSet<Page>();
		try
		{
			while(rs.next())
			{
				pages.add( getPageFromId(rs.getInt("id")) );
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error executing outgoing link query!");
			e.printStackTrace();
			return null;
		}
		
		return pages;
	}
	
	
	/**
	 * Gets a page's name from the database.
	 * @param p The page whose name to retrieve.
	 * @return The string name of the page.
	 */
	public static String getPageName(Page p)
	{
		String query = "SELECT P.page_title as name FROM page as P WHERE P.page_id="+p.pageId+";";
		ResultSet rs = DBManager.query(query);
		if (rs==null){
			return null;
		}
		
		try
		{
			rs.next();
			return rs.getString("name");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves a Page object by id either from the cache or the database.
	 * @param id The integer id of the Page object to retrieve.
	 * @return The Page object represented by the given id
	 */
	private static Page getPageFromId(int id) {
		//See if we have the page cached
		Page page = loadedPages.get(id);
		if (page==null)
		{
			//If not, go ahead and build then cache it
			page = new Page(id);
			loadedPages.put(id, page);
		}
		
		return page;
	}
	
}
