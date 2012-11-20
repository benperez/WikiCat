import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
	 */
	public static Set<Page> getOutgoingLinks(Page page)
	{
		//String query
		//DBManager.query(query);
		return null;
	}
	
	
	/**
	 * Retrieves a Page object by id either from the cache or the database.
	 * @param id The integer id of the Page object to retrieve.
	 * @return The Page object represented by the given id
	 */
	private static Page getPageFromId(int id) {
		//See if we have the page cached
		Page page = loadedPages.get(id);
		if (page!=null)
		{
			return page;
		}
		//Otherwise, go ahead and build it then cache it
		page = new Page(id);
		loadedPages.put(id, page);
		return page;
	}
	
}
